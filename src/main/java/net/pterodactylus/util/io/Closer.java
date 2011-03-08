/*
 * utils - Closer.java - Copyright © 2006-2009 David Roden
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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.logging.Logging;

/**
 * Helper class that can close all kinds of resources without throwing exception
 * so that clean-up code can be written with less code. All methods check that
 * the given resource is not <code>null</code> before invoking the close()
 * method of the respective type.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Closer {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(Closer.class.getName());

	/**
	 * Closes the given result set.
	 *
	 * @param resultSet
	 *            The result set to close
	 * @see ResultSet#close()
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given statement.
	 *
	 * @param statement
	 *            The statement to close
	 * @see Statement#close()
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given connection.
	 *
	 * @param connection
	 *            The connection to close
	 * @see Connection#close()
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given server socket.
	 *
	 * @param serverSocket
	 *            The server socket to close
	 * @see ServerSocket#close()
	 */
	public static void close(ServerSocket serverSocket) {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given socket.
	 *
	 * @param socket
	 *            The socket to close
	 * @see Socket#close()
	 */
	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given input stream.
	 *
	 * @param inputStream
	 *            The input stream to close
	 * @see InputStream#close()
	 */
	public static void close(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given output stream.
	 *
	 * @param outputStream
	 *            The output stream to close
	 * @see OutputStream#close()
	 */
	public static void close(OutputStream outputStream) {
		if (outputStream != null) {
			flush(outputStream);
			try {
				outputStream.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given reader.
	 *
	 * @param reader
	 *            The reader to close
	 * @see Reader#close()
	 */
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given writer.
	 *
	 * @param writer
	 *            The write to close
	 * @see Writer#close()
	 */
	public static void close(Writer writer) {
		if (writer != null) {
			flush(writer);
			try {
				writer.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given jar file.
	 *
	 * @param jarFile
	 *            The JAR file to close
	 * @see JarFile#close()
	 */
	public static void close(JarFile jarFile) {
		if (jarFile != null) {
			try {
				jarFile.close();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Closes the given {@link Closeable}, {@link #flush(Object)}ing it first.
	 *
	 * @param closeable
	 *            The {@link Closeable} to close
	 * @see Closeable#close()
	 */
	public static void close(Closeable closeable) {
		if (closeable == null) {
			return;
		}
		flush(closeable);
		try {
			closeable.close();
		} catch (IOException ioe1) {
			/* ignore. */
		}
	}

	/**
	 * Tries to call the close() method on the given object.
	 *
	 * @param object
	 *            The object to call the close() method on
	 */
	public static void close(Object object) {
		flush(object);
		if (object == null) {
			return;
		}
		try {
			Method closeMethod = object.getClass().getMethod("close");
			closeMethod.invoke(object);
		} catch (SecurityException se1) {
			logger.log(Level.WARNING, "Could not call close() method on " + object, se1);
		} catch (NoSuchMethodException e1) {
			/* ignore. */
		} catch (IllegalArgumentException iae1) {
			logger.log(Level.WARNING, "Illegal argument for close() method on " + object, iae1);
		} catch (IllegalAccessException iae1) {
			logger.log(Level.WARNING, "Could not call close() method on " + object, iae1);
		} catch (InvocationTargetException e1) {
			/* ignore. */
		}
	}

	/**
	 * Flushes the given flushable, swallowing any {@link IOException} that may
	 * occur.
	 *
	 * @param flushable
	 *            The flushable
	 */
	public static void flush(Flushable flushable) {
		if (flushable != null) {
			try {
				flushable.flush();
			} catch (IOException ioe1) {
				/* ignore. */
			}
		}
	}

	/**
	 * Flushes the given object by calling a method called “flush” without
	 * parameters, swallowing any {@link IOException} that may occur.
	 *
	 * @param object
	 *            The object to flush
	 */
	public static void flush(Object object) {
		if (object == null) {
			return;
		}
		try {
			Method flushMethod = object.getClass().getMethod("flush");
			flushMethod.invoke(object);
		} catch (SecurityException se1) {
			logger.log(Level.WARNING, "Could not call flush() method on " + object, se1);
		} catch (NoSuchMethodException e1) {
			/* ignore. */
		} catch (IllegalArgumentException iae1) {
			logger.log(Level.WARNING, "Illegal argument for flush() method on " + object, iae1);
		} catch (IllegalAccessException iae1) {
			logger.log(Level.WARNING, "Could not call flush() method on " + object, iae1);
		} catch (InvocationTargetException e1) {
			/* ignore. */
		}
	}

}
