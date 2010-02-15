/*
 * utils - BitShiftedOutputStream.java - Copyright © 2006-2010 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.util.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.pterodactylus.util.number.Bits;

/**
 * A bit-shifted output stream can write an (almost) arbitrary amount of bits
 * for a value given to {@link #write(int)}. Due to implementation reasons the
 * amount of bits should be between 1 and 32, inclusive. Also note that you can
 * not use the {@link OutputStream#write(byte[])} or
 * {@link OutputStream#write(byte[], int, int)} methods because they will
 * truncate your value to the lowest eight bits which is of course only a
 * problem if you intend to write values larger than eight bits.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class BitShiftedOutputStream extends FilterOutputStream {

	/** The number of bits to write for a value. */
	private final int valueSize;

	/** The current bit position. */
	private int currentBitPosition;

	/** The current value. */
	private int currentValue;

	/**
	 * Creates a new bit-shifted output stream that writes
	 * <code>valueSize</code> bits per value.
	 *
	 * @param outputStream
	 *            The underlying output stream
	 * @param valueSize
	 *            The number of bits to write with one {@link #write(int)}
	 *            instruction
	 * @throws IllegalArgumentException
	 *             if <code>valueSize</code> is not in the range of 1 to 32,
	 *             inclusive
	 */
	public BitShiftedOutputStream(OutputStream outputStream, int valueSize) throws IllegalArgumentException {
		super(outputStream);
		if ((valueSize < 1) || (valueSize > 32)) {
			throw new IllegalArgumentException("valueSize out of range [1-32]");
		}
		this.valueSize = valueSize;
		this.currentBitPosition = 0;
		this.currentValue = 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.FilterOutputStream#write(int)
	 */
	@Override
	public void write(int value) throws IOException {
		int valueLeft = value;
		int bitsLeft = valueSize;
		while (bitsLeft > 0) {
			int bitsToEncode = Math.min(8, Math.min(bitsLeft, 8 - currentBitPosition));
			currentValue = Bits.encodeBits(currentValue, currentBitPosition, bitsToEncode, valueLeft);
			valueLeft >>>= bitsToEncode;
			currentBitPosition += bitsToEncode;
			bitsLeft -= bitsToEncode;
			if (currentBitPosition == 8) {
				super.write(currentValue & 0xff);
				currentBitPosition = 0;
				currentValue = -1;
			}
		}
	}

	/**
	 * Writes the specified number of zero-bits to this stream.
	 *
	 * @param numberOfBits
	 *            The number of zero-bits to write
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void writePadding(int numberOfBits) throws IOException {
		writePadding(numberOfBits, 0x00);
	}

	/**
	 * Writes the specified number of bits to the stream. The bit used to pad
	 * the stream is the lowest bit of <code>fillBit</code>.
	 *
	 * @param numberOfBits
	 *            The number of padding bits to write
	 * @param fillBit
	 *            Contains at the lowest bit position the padding bit to write
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void writePadding(int numberOfBits, int fillBit) throws IOException {
		int bitsLeft = numberOfBits;
		while (bitsLeft > 0) {
			currentValue = Bits.encodeBits(currentValue, currentBitPosition, 1, fillBit);
			currentBitPosition++;
			bitsLeft--;
			if (currentBitPosition == 8) {
				super.write(currentValue & 0xff);
				currentBitPosition = 0;
				currentValue = -1;
			}
		}
	}

	/**
	 * Flushes all unwritten data to the underlying output stream.
	 * <p>
	 * This is a convenience method for {@link #flush(int) flush(0x00)} which
	 * will zero-pad the unused bits in the last byte.
	 *
	 * @see java.io.FilterOutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		flush(0x00);
	}

	/**
	 * Flushes this output stream, writing all unwritten values to the
	 * underlying output stream.
	 *
	 * @param fillBit
	 *            The lowest bit of this value will determine what the unused
	 *            space in a byte is filled with
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void flush(int fillBit) throws IOException {
		if (currentBitPosition != 0) {
			currentValue &= (0xff >> (8 - currentBitPosition));
			for (int bit = currentBitPosition; bit < 8; bit++) {
				currentValue = Bits.encodeBits(currentValue, bit, 1, fillBit & 0x01);
			}
			currentBitPosition = 0;
			super.write(currentValue);
			currentValue = -1;
		}
		super.flush();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		flush();
		super.close();
	}

}
