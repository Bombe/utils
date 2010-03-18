/*
 * utils - MimeTypes.java - Copyright © 2008-2010 David Roden
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles MIME types and maps them to file extensions.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class MimeTypes {

	/** The default MIME type for unknown extensions. */
	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	/** List of all MIME types. */
	private static final List<String> mimeTypes = new ArrayList<String>();

	/** Maps from MIME types to registered extensions. */
	private static final Map<String, List<String>> mimeTypeExtensions = new HashMap<String, List<String>>();

	/** Maps from extensions to registered MIME types. */
	private static final Map<String, List<String>> extensionMimeTypes = new HashMap<String, List<String>>();

	/* MIME type list generated from my /etc/mime.types. */
	static {
		addMimeType("application/activemessage");
		addMimeType("application/andrew-inset", "ez");
		addMimeType("application/applefile");
		addMimeType("application/atomicmail");
		addMimeType("application/batch-SMTP");
		addMimeType("application/beep+xml");
		addMimeType("application/cals-1840");
		addMimeType("application/commonground");
		addMimeType("application/cu-seeme", "cu");
		addMimeType("application/cybercash");
		addMimeType("application/dca-rft");
		addMimeType("application/dec-dx");
		addMimeType("application/docbook+xml");
		addMimeType("application/dsptype", "tsp");
		addMimeType("application/dvcs");
		addMimeType("application/edi-consent");
		addMimeType("application/edi-x12");
		addMimeType("application/edifact");
		addMimeType("application/eshop");
		addMimeType("application/font-tdpfr");
		addMimeType("application/futuresplash", "spl");
		addMimeType("application/ghostview");
		addMimeType("application/hta", "hta");
		addMimeType("application/http");
		addMimeType("application/hyperstudio");
		addMimeType("application/iges");
		addMimeType("application/index");
		addMimeType("application/index.cmd");
		addMimeType("application/index.obj");
		addMimeType("application/index.response");
		addMimeType("application/index.vnd");
		addMimeType("application/iotp");
		addMimeType("application/ipp");
		addMimeType("application/isup");
		addMimeType("application/java-archive", "jar");
		addMimeType("application/java-serialized-object", "ser");
		addMimeType("application/java-vm", "class");
		addMimeType("application/mac-binhex40", "hqx");
		addMimeType("application/mac-compactpro", "cpt");
		addMimeType("application/macwriteii");
		addMimeType("application/marc");
		addMimeType("application/mathematica", "nb");
		addMimeType("application/mathematica-old");
		addMimeType("application/msaccess", "mdb");
		addMimeType("application/msword", "doc", "dot");
		addMimeType("application/news-message-id");
		addMimeType("application/news-transmission");
		addMimeType("application/ocsp-request");
		addMimeType("application/ocsp-response");
		addMimeType("application/octet-stream", "bin");
		addMimeType("application/oda", "oda");
		addMimeType("application/ogg", "ogg");
		addMimeType("application/parityfec");
		addMimeType("application/pdf", "pdf");
		addMimeType("application/pgp-encrypted");
		addMimeType("application/pgp-keys", "key");
		addMimeType("application/pgp-signature", "pgp");
		addMimeType("application/pics-rules", "prf");
		addMimeType("application/pkcs10");
		addMimeType("application/pkcs7-mime");
		addMimeType("application/pkcs7-signature");
		addMimeType("application/pkix-cert");
		addMimeType("application/pkix-crl");
		addMimeType("application/pkixcmp");
		addMimeType("application/postscript", "ps", "ai", "eps");
		addMimeType("application/prs.alvestrand.titrax-sheet");
		addMimeType("application/prs.cww");
		addMimeType("application/prs.nprend");
		addMimeType("application/qsig");
		addMimeType("application/rar", "rar");
		addMimeType("application/rdf+xml", "rdf");
		addMimeType("application/remote-printing");
		addMimeType("application/riscos");
		addMimeType("application/rss+xml", "rss");
		addMimeType("application/rtf");
		addMimeType("application/sdp");
		addMimeType("application/set-payment");
		addMimeType("application/set-payment-initiation");
		addMimeType("application/set-registration");
		addMimeType("application/set-registration-initiation");
		addMimeType("application/sgml");
		addMimeType("application/sgml-open-catalog");
		addMimeType("application/sieve");
		addMimeType("application/slate");
		addMimeType("application/smil", "smi", "smil");
		addMimeType("application/timestamp-query");
		addMimeType("application/timestamp-reply");
		addMimeType("application/vemmi");
		addMimeType("application/whoispp-query");
		addMimeType("application/whoispp-response");
		addMimeType("application/wita");
		addMimeType("application/wordperfect", "wpd");
		addMimeType("application/wordperfect5.1", "wp5");
		addMimeType("application/x400-bp");
		addMimeType("application/xhtml+xml", "xhtml", "xht");
		addMimeType("application/xml", "xml", "xsl");
		addMimeType("application/xml-dtd");
		addMimeType("application/xml-external-parsed-entity");
		addMimeType("application/zip", "zip");
		addMimeType("application/vnd.3M.Post-it-Notes");
		addMimeType("application/vnd.accpac.simply.aso");
		addMimeType("application/vnd.accpac.simply.imp");
		addMimeType("application/vnd.acucobol");
		addMimeType("application/vnd.aether.imp");
		addMimeType("application/vnd.anser-web-certificate-issue-initiation");
		addMimeType("application/vnd.anser-web-funds-transfer-initiation");
		addMimeType("application/vnd.audiograph");
		addMimeType("application/vnd.bmi");
		addMimeType("application/vnd.businessobjects");
		addMimeType("application/vnd.canon-cpdl");
		addMimeType("application/vnd.canon-lips");
		addMimeType("application/vnd.cinderella", "cdy");
		addMimeType("application/vnd.claymore");
		addMimeType("application/vnd.commerce-battelle");
		addMimeType("application/vnd.commonspace");
		addMimeType("application/vnd.comsocaller");
		addMimeType("application/vnd.contact.cmsg");
		addMimeType("application/vnd.cosmocaller");
		addMimeType("application/vnd.ctc-posml");
		addMimeType("application/vnd.cups-postscript");
		addMimeType("application/vnd.cups-raster");
		addMimeType("application/vnd.cups-raw");
		addMimeType("application/vnd.cybank");
		addMimeType("application/vnd.dna");
		addMimeType("application/vnd.dpgraph");
		addMimeType("application/vnd.dxr");
		addMimeType("application/vnd.ecdis-update");
		addMimeType("application/vnd.ecowin.chart");
		addMimeType("application/vnd.ecowin.filerequest");
		addMimeType("application/vnd.ecowin.fileupdate");
		addMimeType("application/vnd.ecowin.series");
		addMimeType("application/vnd.ecowin.seriesrequest");
		addMimeType("application/vnd.ecowin.seriesupdate");
		addMimeType("application/vnd.enliven");
		addMimeType("application/vnd.epson.esf");
		addMimeType("application/vnd.epson.msf");
		addMimeType("application/vnd.epson.quickanime");
		addMimeType("application/vnd.epson.salt");
		addMimeType("application/vnd.epson.ssf");
		addMimeType("application/vnd.ericsson.quickcall");
		addMimeType("application/vnd.eudora.data");
		addMimeType("application/vnd.fdf");
		addMimeType("application/vnd.ffsns");
		addMimeType("application/vnd.flographit");
		addMimeType("application/vnd.framemaker");
		addMimeType("application/vnd.fsc.weblaunch");
		addMimeType("application/vnd.fujitsu.oasys");
		addMimeType("application/vnd.fujitsu.oasys2");
		addMimeType("application/vnd.fujitsu.oasys3");
		addMimeType("application/vnd.fujitsu.oasysgp");
		addMimeType("application/vnd.fujitsu.oasysprs");
		addMimeType("application/vnd.fujixerox.ddd");
		addMimeType("application/vnd.fujixerox.docuworks");
		addMimeType("application/vnd.fujixerox.docuworks.binder");
		addMimeType("application/vnd.fut-misnet");
		addMimeType("application/vnd.grafeq");
		addMimeType("application/vnd.groove-account");
		addMimeType("application/vnd.groove-identity-message");
		addMimeType("application/vnd.groove-injector");
		addMimeType("application/vnd.groove-tool-message");
		addMimeType("application/vnd.groove-tool-template");
		addMimeType("application/vnd.groove-vcard");
		addMimeType("application/vnd.hhe.lesson-player");
		addMimeType("application/vnd.hp-HPGL");
		addMimeType("application/vnd.hp-PCL");
		addMimeType("application/vnd.hp-PCLXL");
		addMimeType("application/vnd.hp-hpid");
		addMimeType("application/vnd.hp-hps");
		addMimeType("application/vnd.httphone");
		addMimeType("application/vnd.hzn-3d-crossword");
		addMimeType("application/vnd.ibm.MiniPay");
		addMimeType("application/vnd.ibm.afplinedata");
		addMimeType("application/vnd.ibm.modcap");
		addMimeType("application/vnd.informix-visionary");
		addMimeType("application/vnd.intercon.formnet");
		addMimeType("application/vnd.intertrust.digibox");
		addMimeType("application/vnd.intertrust.nncp");
		addMimeType("application/vnd.intu.qbo");
		addMimeType("application/vnd.intu.qfx");
		addMimeType("application/vnd.irepository.package+xml");
		addMimeType("application/vnd.is-xpr");
		addMimeType("application/vnd.japannet-directory-service");
		addMimeType("application/vnd.japannet-jpnstore-wakeup");
		addMimeType("application/vnd.japannet-payment-wakeup");
		addMimeType("application/vnd.japannet-registration");
		addMimeType("application/vnd.japannet-registration-wakeup");
		addMimeType("application/vnd.japannet-setstore-wakeup");
		addMimeType("application/vnd.japannet-verification");
		addMimeType("application/vnd.japannet-verification-wakeup");
		addMimeType("application/vnd.koan");
		addMimeType("application/vnd.lotus-1-2-3");
		addMimeType("application/vnd.lotus-approach");
		addMimeType("application/vnd.lotus-freelance");
		addMimeType("application/vnd.lotus-notes");
		addMimeType("application/vnd.lotus-organizer");
		addMimeType("application/vnd.lotus-screencam");
		addMimeType("application/vnd.lotus-wordpro");
		addMimeType("application/vnd.mcd");
		addMimeType("application/vnd.mediastation.cdkey");
		addMimeType("application/vnd.meridian-slingshot");
		addMimeType("application/vnd.mif");
		addMimeType("application/vnd.minisoft-hp3000-save");
		addMimeType("application/vnd.mitsubishi.misty-guard.trustweb");
		addMimeType("application/vnd.mobius.daf");
		addMimeType("application/vnd.mobius.dis");
		addMimeType("application/vnd.mobius.msl");
		addMimeType("application/vnd.mobius.plc");
		addMimeType("application/vnd.mobius.txf");
		addMimeType("application/vnd.motorola.flexsuite");
		addMimeType("application/vnd.motorola.flexsuite.adsi");
		addMimeType("application/vnd.motorola.flexsuite.fis");
		addMimeType("application/vnd.motorola.flexsuite.gotap");
		addMimeType("application/vnd.motorola.flexsuite.kmr");
		addMimeType("application/vnd.motorola.flexsuite.ttc");
		addMimeType("application/vnd.motorola.flexsuite.wem");
		addMimeType("application/vnd.mozilla.xul+xml", "xul");
		addMimeType("application/vnd.ms-artgalry");
		addMimeType("application/vnd.ms-asf");
		addMimeType("application/vnd.ms-excel", "xls", "xlb", "xlt");
		addMimeType("application/vnd.ms-lrm");
		addMimeType("application/vnd.ms-pki.seccat", "cat");
		addMimeType("application/vnd.ms-pki.stl", "stl");
		addMimeType("application/vnd.ms-powerpoint", "ppt", "pps");
		addMimeType("application/vnd.ms-project");
		addMimeType("application/vnd.ms-tnef");
		addMimeType("application/vnd.ms-works");
		addMimeType("application/vnd.mseq");
		addMimeType("application/vnd.msign");
		addMimeType("application/vnd.music-niff");
		addMimeType("application/vnd.musician");
		addMimeType("application/vnd.netfpx");
		addMimeType("application/vnd.noblenet-directory");
		addMimeType("application/vnd.noblenet-sealer");
		addMimeType("application/vnd.noblenet-web");
		addMimeType("application/vnd.novadigm.EDM");
		addMimeType("application/vnd.novadigm.EDX");
		addMimeType("application/vnd.novadigm.EXT");
		addMimeType("application/vnd.oasis.opendocument.chart", "odc");
		addMimeType("application/vnd.oasis.opendocument.database", "odb");
		addMimeType("application/vnd.oasis.opendocument.formula", "odf");
		addMimeType("application/vnd.oasis.opendocument.graphics", "odg");
		addMimeType("application/vnd.oasis.opendocument.graphics-template", "otg");
		addMimeType("application/vnd.oasis.opendocument.image", "odi");
		addMimeType("application/vnd.oasis.opendocument.presentation", "odp");
		addMimeType("application/vnd.oasis.opendocument.presentation-template", "otp");
		addMimeType("application/vnd.oasis.opendocument.spreadsheet", "ods");
		addMimeType("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
		addMimeType("application/vnd.oasis.opendocument.text", "odt");
		addMimeType("application/vnd.oasis.opendocument.text-master", "odm");
		addMimeType("application/vnd.oasis.opendocument.text-template", "ott");
		addMimeType("application/vnd.oasis.opendocument.text-web", "oth");
		addMimeType("application/vnd.osa.netdeploy");
		addMimeType("application/vnd.palm");
		addMimeType("application/vnd.pg.format");
		addMimeType("application/vnd.pg.osasli");
		addMimeType("application/vnd.powerbuilder6");
		addMimeType("application/vnd.powerbuilder6-s");
		addMimeType("application/vnd.powerbuilder7");
		addMimeType("application/vnd.powerbuilder7-s");
		addMimeType("application/vnd.powerbuilder75");
		addMimeType("application/vnd.powerbuilder75-s");
		addMimeType("application/vnd.previewsystems.box");
		addMimeType("application/vnd.publishare-delta-tree");
		addMimeType("application/vnd.pvi.ptid1");
		addMimeType("application/vnd.pwg-xhtml-print+xml");
		addMimeType("application/vnd.rapid");
		addMimeType("application/vnd.rim.cod", "cod");
		addMimeType("application/vnd.s3sms");
		addMimeType("application/vnd.seemail");
		addMimeType("application/vnd.shana.informed.formdata");
		addMimeType("application/vnd.shana.informed.formtemplate");
		addMimeType("application/vnd.shana.informed.interchange");
		addMimeType("application/vnd.shana.informed.package");
		addMimeType("application/vnd.smaf", "mmf");
		addMimeType("application/vnd.sss-cod");
		addMimeType("application/vnd.sss-dtf");
		addMimeType("application/vnd.sss-ntf");
		addMimeType("application/vnd.stardivision.calc", "sdc");
		addMimeType("application/vnd.stardivision.draw", "sda");
		addMimeType("application/vnd.stardivision.impress", "sdd", "sdp");
		addMimeType("application/vnd.stardivision.math", "smf");
		addMimeType("application/vnd.stardivision.writer", "sdw", "vor");
		addMimeType("application/vnd.stardivision.writer-global", "sgl");
		addMimeType("application/vnd.street-stream");
		addMimeType("application/vnd.sun.xml.calc", "sxc");
		addMimeType("application/vnd.sun.xml.calc.template", "stc");
		addMimeType("application/vnd.sun.xml.draw", "sxd");
		addMimeType("application/vnd.sun.xml.draw.template", "std");
		addMimeType("application/vnd.sun.xml.impress", "sxi");
		addMimeType("application/vnd.sun.xml.impress.template", "sti");
		addMimeType("application/vnd.sun.xml.math", "sxm");
		addMimeType("application/vnd.sun.xml.writer", "sxw");
		addMimeType("application/vnd.sun.xml.writer.global", "sxg");
		addMimeType("application/vnd.sun.xml.writer.template", "stw");
		addMimeType("application/vnd.svd");
		addMimeType("application/vnd.swiftview-ics");
		addMimeType("application/vnd.symbian.install", "sis");
		addMimeType("application/vnd.triscape.mxs");
		addMimeType("application/vnd.trueapp");
		addMimeType("application/vnd.truedoc");
		addMimeType("application/vnd.tve-trigger");
		addMimeType("application/vnd.ufdl");
		addMimeType("application/vnd.uplanet.alert");
		addMimeType("application/vnd.uplanet.alert-wbxml");
		addMimeType("application/vnd.uplanet.bearer-choice");
		addMimeType("application/vnd.uplanet.bearer-choice-wbxml");
		addMimeType("application/vnd.uplanet.cacheop");
		addMimeType("application/vnd.uplanet.cacheop-wbxml");
		addMimeType("application/vnd.uplanet.channel");
		addMimeType("application/vnd.uplanet.channel-wbxml");
		addMimeType("application/vnd.uplanet.list");
		addMimeType("application/vnd.uplanet.list-wbxml");
		addMimeType("application/vnd.uplanet.listcmd");
		addMimeType("application/vnd.uplanet.listcmd-wbxml");
		addMimeType("application/vnd.uplanet.signal");
		addMimeType("application/vnd.vcx");
		addMimeType("application/vnd.vectorworks");
		addMimeType("application/vnd.vidsoft.vidconference");
		addMimeType("application/vnd.visio", "vsd");
		addMimeType("application/vnd.vividence.scriptfile");
		addMimeType("application/vnd.wap.sic");
		addMimeType("application/vnd.wap.slc");
		addMimeType("application/vnd.wap.wbxml", "wbxml");
		addMimeType("application/vnd.wap.wmlc", "wmlc");
		addMimeType("application/vnd.wap.wmlscriptc", "wmlsc");
		addMimeType("application/vnd.webturbo");
		addMimeType("application/vnd.wrq-hp3000-labelled");
		addMimeType("application/vnd.wt.stf");
		addMimeType("application/vnd.xara");
		addMimeType("application/vnd.xfdl");
		addMimeType("application/vnd.yellowriver-custom-menu");
		addMimeType("application/x-123", "wk");
		addMimeType("application/x-abiword", "abw");
		addMimeType("application/x-apple-diskimage", "dmg");
		addMimeType("application/x-bcpio", "bcpio");
		addMimeType("application/x-bittorrent", "torrent");
		addMimeType("application/x-cdf", "cdf");
		addMimeType("application/x-cdlink", "vcd");
		addMimeType("application/x-chess-pgn", "pgn");
		addMimeType("application/x-core");
		addMimeType("application/x-cpio", "cpio");
		addMimeType("application/x-csh", "csh");
		addMimeType("application/x-debian-package", "deb", "udeb");
		addMimeType("application/x-director", "dcr", "dir", "dxr");
		addMimeType("application/x-dms", "dms");
		addMimeType("application/x-doom", "wad");
		addMimeType("application/x-dvi", "dvi");
		addMimeType("application/x-executable");
		addMimeType("application/x-flac", "flac");
		addMimeType("application/x-font", "pfa", "pfb", "gsf", "pcf", "pcf.Z");
		addMimeType("application/x-freemind", "mm");
		addMimeType("application/x-futuresplash", "spl");
		addMimeType("application/x-gnumeric", "gnumeric");
		addMimeType("application/x-go-sgf", "sgf");
		addMimeType("application/x-graphing-calculator", "gcf");
		addMimeType("application/x-gtar", "gtar", "tgz", "taz");
		addMimeType("application/x-hdf", "hdf");
		addMimeType("application/x-ica", "ica");
		addMimeType("application/x-internet-signup", "ins", "isp");
		addMimeType("application/x-iphone", "iii");
		addMimeType("application/x-iso9660-image", "iso");
		addMimeType("application/x-java-applet");
		addMimeType("application/x-java-bean");
		addMimeType("application/x-java-jnlp-file", "jnlp");
		addMimeType("application/x-javascript", "js");
		addMimeType("application/x-jmol", "jmz");
		addMimeType("application/x-kchart", "chrt");
		addMimeType("application/x-kdelnk");
		addMimeType("application/x-killustrator", "kil");
		addMimeType("application/x-koan", "skp", "skd", "skt", "skm");
		addMimeType("application/x-kpresenter", "kpr", "kpt");
		addMimeType("application/x-kspread", "ksp");
		addMimeType("application/x-kword", "kwd", "kwt");
		addMimeType("application/x-latex", "latex");
		addMimeType("application/x-lha", "lha");
		addMimeType("application/x-lzh", "lzh");
		addMimeType("application/x-lzx", "lzx");
		addMimeType("application/x-maker", "frm", "maker", "frame", "fm", "fb", "book", "fbdoc");
		addMimeType("application/x-mif", "mif");
		addMimeType("application/x-ms-wmd", "wmd");
		addMimeType("application/x-ms-wmz", "wmz");
		addMimeType("application/x-msdos-program", "com", "exe", "bat", "dll");
		addMimeType("application/x-msi", "msi");
		addMimeType("application/x-netcdf", "nc");
		addMimeType("application/x-ns-proxy-autoconfig", "pac");
		addMimeType("application/x-nwc", "nwc");
		addMimeType("application/x-object", "o");
		addMimeType("application/x-oz-application", "oza");
		addMimeType("application/x-pkcs7-certreqresp", "p7r");
		addMimeType("application/x-pkcs7-crl", "crl");
		addMimeType("application/x-python-code", "pyc", "pyo");
		addMimeType("application/x-quicktimeplayer", "qtl");
		addMimeType("application/x-redhat-package-manager", "rpm");
		addMimeType("application/x-rx");
		addMimeType("application/x-sh", "sh");
		addMimeType("application/x-shar", "shar");
		addMimeType("application/x-shellscript");
		addMimeType("application/x-shockwave-flash", "swf", "swfl");
		addMimeType("application/x-stuffit", "sit");
		addMimeType("application/x-sv4cpio", "sv4cpio");
		addMimeType("application/x-sv4crc", "sv4crc");
		addMimeType("application/x-tar", "tar");
		addMimeType("application/x-tcl", "tcl");
		addMimeType("application/x-tex-gf", "gf");
		addMimeType("application/x-tex-pk", "pk");
		addMimeType("application/x-texinfo", "texinfo", "texi");
		addMimeType("application/x-trash", "~", "%", "bak", "old", "sik");
		addMimeType("application/x-troff", "t", "tr", "roff");
		addMimeType("application/x-troff-man", "man");
		addMimeType("application/x-troff-me", "me");
		addMimeType("application/x-troff-ms", "ms");
		addMimeType("application/x-ustar", "ustar");
		addMimeType("application/x-videolan");
		addMimeType("application/x-wais-source", "src");
		addMimeType("application/x-wingz", "wz");
		addMimeType("application/x-x509-ca-cert", "crt");
		addMimeType("application/x-xcf", "xcf");
		addMimeType("application/x-xfig", "fig");
		addMimeType("application/x-xpinstall", "xpi");
		addMimeType("audio/32kadpcm");
		addMimeType("audio/basic", "au", "snd");
		addMimeType("audio/dvi4");
		addMimeType("audio/g.722.1");
		addMimeType("audio/g722");
		addMimeType("audio/g723");
		addMimeType("audio/g726-16");
		addMimeType("audio/g726-24");
		addMimeType("audio/g726-32");
		addMimeType("audio/g726-40");
		addMimeType("audio/g728");
		addMimeType("audio/g729");
		addMimeType("audio/g729d");
		addMimeType("audio/g729e");
		addMimeType("audio/gsm");
		addMimeType("audio/gsm-efr");
		addMimeType("audio/l8");
		addMimeType("audio/l16");
		addMimeType("audio/lpc");
		addMimeType("audio/midi", "mid", "midi", "kar");
		addMimeType("audio/mp4a-latm");
		addMimeType("audio/mpa");
		addMimeType("audio/mpa-robust");
		addMimeType("audio/mpeg", "mpga", "mpega", "mp2", "mp3", "m4a");
		addMimeType("audio/mpegurl", "m3u");
		addMimeType("audio/parityfec");
		addMimeType("audio/pcma");
		addMimeType("audio/pcmu");
		addMimeType("audio/prs.sid", "sid");
		addMimeType("audio/qcelp");
		addMimeType("audio/red");
		addMimeType("audio/telephone-event");
		addMimeType("audio/tone");
		addMimeType("audio/vdvi");
		addMimeType("audio/vnd.cisco.nse");
		addMimeType("audio/vnd.cns.anp1");
		addMimeType("audio/vnd.cns.inf1");
		addMimeType("audio/vnd.digital-winds");
		addMimeType("audio/vnd.everad.plj");
		addMimeType("audio/vnd.lucent.voice");
		addMimeType("audio/vnd.nortel.vbk");
		addMimeType("audio/vnd.nuera.ecelp4800");
		addMimeType("audio/vnd.nuera.ecelp7470");
		addMimeType("audio/vnd.nuera.ecelp9600");
		addMimeType("audio/vnd.octel.sbc");
		addMimeType("audio/vnd.qcelp");
		addMimeType("audio/vnd.rhetorex.32kadpcm");
		addMimeType("audio/vnd.vmx.cvsd");
		addMimeType("audio/x-aiff", "aif", "aiff", "aifc");
		addMimeType("audio/x-gsm", "gsm");
		addMimeType("audio/x-mpegurl", "m3u");
		addMimeType("audio/x-ms-wma", "wma");
		addMimeType("audio/x-ms-wax", "wax");
		addMimeType("audio/x-pn-realaudio-plugin");
		addMimeType("audio/x-pn-realaudio", "ra", "rm", "ram");
		addMimeType("audio/x-realaudio", "ra");
		addMimeType("audio/x-scpls", "pls");
		addMimeType("audio/x-sd2", "sd2");
		addMimeType("audio/x-wav", "wav");
		addMimeType("chemical/x-alchemy", "alc");
		addMimeType("chemical/x-cache", "cac", "cache");
		addMimeType("chemical/x-cache-csf", "csf");
		addMimeType("chemical/x-cactvs-binary", "cbin", "cascii", "ctab");
		addMimeType("chemical/x-cdx", "cdx");
		addMimeType("chemical/x-cerius", "cer");
		addMimeType("chemical/x-chem3d", "c3d");
		addMimeType("chemical/x-chemdraw", "chm");
		addMimeType("chemical/x-cif", "cif");
		addMimeType("chemical/x-cmdf", "cmdf");
		addMimeType("chemical/x-cml", "cml");
		addMimeType("chemical/x-compass", "cpa");
		addMimeType("chemical/x-crossfire", "bsd");
		addMimeType("chemical/x-csml", "csml", "csm");
		addMimeType("chemical/x-ctx", "ctx");
		addMimeType("chemical/x-cxf", "cxf", "cef");
		addMimeType("chemical/x-embl-dl-nucleotide", "emb", "embl");
		addMimeType("chemical/x-galactic-spc", "spc");
		addMimeType("chemical/x-gamess-input", "inp", "gam", "gamin");
		addMimeType("chemical/x-gaussian-checkpoint", "fch", "fchk");
		addMimeType("chemical/x-gaussian-cube", "cub");
		addMimeType("chemical/x-gaussian-input", "gau", "gjc", "gjf");
		addMimeType("chemical/x-gaussian-log", "gal");
		addMimeType("chemical/x-gcg8-sequence", "gcg");
		addMimeType("chemical/x-genbank", "gen");
		addMimeType("chemical/x-hin", "hin");
		addMimeType("chemical/x-isostar", "istr", "ist");
		addMimeType("chemical/x-jcamp-dx", "jdx", "dx");
		addMimeType("chemical/x-kinemage", "kin");
		addMimeType("chemical/x-macmolecule", "mcm");
		addMimeType("chemical/x-macromodel-input", "mmd", "mmod");
		addMimeType("chemical/x-mdl-molfile", "mol");
		addMimeType("chemical/x-mdl-rdfile", "rd");
		addMimeType("chemical/x-mdl-rxnfile", "rxn");
		addMimeType("chemical/x-mdl-sdfile", "sd", "sdf");
		addMimeType("chemical/x-mdl-tgf", "tgf");
		addMimeType("chemical/x-mmcif", "mcif");
		addMimeType("chemical/x-mol2", "mol2");
		addMimeType("chemical/x-molconn-Z", "b");
		addMimeType("chemical/x-mopac-graph", "gpt");
		addMimeType("chemical/x-mopac-input", "mop", "mopcrt", "mpc", "dat", "zmt");
		addMimeType("chemical/x-mopac-out", "moo");
		addMimeType("chemical/x-mopac-vib", "mvb");
		addMimeType("chemical/x-ncbi-asn1", "asn");
		addMimeType("chemical/x-ncbi-asn1-ascii", "prt", "ent");
		addMimeType("chemical/x-ncbi-asn1-binary", "val", "aso");
		addMimeType("chemical/x-ncbi-asn1-spec", "asn");
		addMimeType("chemical/x-pdb", "pdb", "ent");
		addMimeType("chemical/x-rosdal", "ros");
		addMimeType("chemical/x-swissprot", "sw");
		addMimeType("chemical/x-vamas-iso14976", "vms");
		addMimeType("chemical/x-vmd", "vmd");
		addMimeType("chemical/x-xtel", "xtel");
		addMimeType("chemical/x-xyz", "xyz");
		addMimeType("image/cgm");
		addMimeType("image/g3fax");
		addMimeType("image/gif", "gif");
		addMimeType("image/ief", "ief");
		addMimeType("image/jpeg", "jpeg", "jpg", "jpe");
		addMimeType("image/naplps");
		addMimeType("image/pcx", "pcx");
		addMimeType("image/png", "png");
		addMimeType("image/prs.btif");
		addMimeType("image/prs.pti");
		addMimeType("image/svg+xml", "svg", "svgz");
		addMimeType("image/tiff", "tiff", "tif");
		addMimeType("image/vnd.cns.inf2");
		addMimeType("image/vnd.djvu", "djvu", "djv");
		addMimeType("image/vnd.dwg");
		addMimeType("image/vnd.dxf");
		addMimeType("image/vnd.fastbidsheet");
		addMimeType("image/vnd.fpx");
		addMimeType("image/vnd.fst");
		addMimeType("image/vnd.fujixerox.edmics-mmr");
		addMimeType("image/vnd.fujixerox.edmics-rlc");
		addMimeType("image/vnd.mix");
		addMimeType("image/vnd.net-fpx");
		addMimeType("image/vnd.svf");
		addMimeType("image/vnd.wap.wbmp", "wbmp");
		addMimeType("image/vnd.xiff");
		addMimeType("image/x-cmu-raster", "ras");
		addMimeType("image/x-coreldraw", "cdr");
		addMimeType("image/x-coreldrawpattern", "pat");
		addMimeType("image/x-coreldrawtemplate", "cdt");
		addMimeType("image/x-corelphotopaint", "cpt");
		addMimeType("image/x-icon", "ico");
		addMimeType("image/x-jg", "art");
		addMimeType("image/x-jng", "jng");
		addMimeType("image/x-ms-bmp", "bmp");
		addMimeType("image/x-photoshop", "psd");
		addMimeType("image/x-portable-anymap", "pnm");
		addMimeType("image/x-portable-bitmap", "pbm");
		addMimeType("image/x-portable-graymap", "pgm");
		addMimeType("image/x-portable-pixmap", "ppm");
		addMimeType("image/x-rgb", "rgb");
		addMimeType("image/x-xbitmap", "xbm");
		addMimeType("image/x-xpixmap", "xpm");
		addMimeType("image/x-xwindowdump", "xwd");
		addMimeType("inode/chardevice");
		addMimeType("inode/blockdevice");
		addMimeType("inode/directory-locked");
		addMimeType("inode/directory");
		addMimeType("inode/fifo");
		addMimeType("inode/socket");
		addMimeType("message/delivery-status");
		addMimeType("message/disposition-notification");
		addMimeType("message/external-body");
		addMimeType("message/http");
		addMimeType("message/s-http");
		addMimeType("message/news");
		addMimeType("message/partial");
		addMimeType("message/rfc822");
		addMimeType("model/iges", "igs", "iges");
		addMimeType("model/mesh", "msh", "mesh", "silo");
		addMimeType("model/vnd.dwf");
		addMimeType("model/vnd.flatland.3dml");
		addMimeType("model/vnd.gdl");
		addMimeType("model/vnd.gs-gdl");
		addMimeType("model/vnd.gtw");
		addMimeType("model/vnd.mts");
		addMimeType("model/vnd.vtu");
		addMimeType("model/vrml", "wrl", "vrml");
		addMimeType("multipart/alternative");
		addMimeType("multipart/appledouble");
		addMimeType("multipart/byteranges");
		addMimeType("multipart/digest");
		addMimeType("multipart/encrypted");
		addMimeType("multipart/form-data");
		addMimeType("multipart/header-set");
		addMimeType("multipart/mixed");
		addMimeType("multipart/parallel");
		addMimeType("multipart/related");
		addMimeType("multipart/report");
		addMimeType("multipart/signed");
		addMimeType("multipart/voice-message");
		addMimeType("text/calendar", "ics", "icz");
		addMimeType("text/comma-separated-values", "csv");
		addMimeType("text/css", "css");
		addMimeType("text/directory");
		addMimeType("text/english");
		addMimeType("text/enriched");
		addMimeType("text/h323", "323");
		addMimeType("text/html", "html", "htm", "shtml");
		addMimeType("text/iuls", "uls");
		addMimeType("text/mathml", "mml");
		addMimeType("text/parityfec");
		addMimeType("text/plain", "asc", "txt", "text", "diff", "pot");
		addMimeType("text/prs.lines.tag");
		addMimeType("text/x-psp", "psp");
		addMimeType("text/rfc822-headers");
		addMimeType("text/richtext", "rtx");
		addMimeType("text/rtf", "rtf");
		addMimeType("text/scriptlet", "sct", "wsc");
		addMimeType("text/t140");
		addMimeType("text/texmacs", "tm", "ts");
		addMimeType("text/tab-separated-values", "tsv");
		addMimeType("text/uri-list");
		addMimeType("text/vnd.abc");
		addMimeType("text/vnd.curl");
		addMimeType("text/vnd.DMClientScript");
		addMimeType("text/vnd.flatland.3dml");
		addMimeType("text/vnd.fly");
		addMimeType("text/vnd.fmi.flexstor");
		addMimeType("text/vnd.in3d.3dml");
		addMimeType("text/vnd.in3d.spot");
		addMimeType("text/vnd.IPTC.NewsML");
		addMimeType("text/vnd.IPTC.NITF");
		addMimeType("text/vnd.latex-z");
		addMimeType("text/vnd.motorola.reflex");
		addMimeType("text/vnd.ms-mediapackage");
		addMimeType("text/vnd.sun.j2me.app-descriptor", "jad");
		addMimeType("text/vnd.wap.si");
		addMimeType("text/vnd.wap.sl");
		addMimeType("text/vnd.wap.wml", "wml");
		addMimeType("text/vnd.wap.wmlscript", "wmls");
		addMimeType("text/x-bibtex", "bib");
		addMimeType("text/x-c++hdr", "h++", "hpp", "hxx", "hh");
		addMimeType("text/x-c++src", "c++", "cpp", "cxx", "cc");
		addMimeType("text/x-chdr", "h");
		addMimeType("text/x-crontab");
		addMimeType("text/x-csh", "csh");
		addMimeType("text/x-csrc", "c");
		addMimeType("text/x-haskell", "hs");
		addMimeType("text/x-java", "java");
		addMimeType("text/x-literate-haskell", "lhs");
		addMimeType("text/x-makefile");
		addMimeType("text/x-moc", "moc");
		addMimeType("text/x-pascal", "p", "pas");
		addMimeType("text/x-pcs-gcd", "gcd");
		addMimeType("text/x-perl", "pl", "pm");
		addMimeType("text/x-python", "py");
		addMimeType("text/x-server-parsed-html");
		addMimeType("text/x-setext", "etx");
		addMimeType("text/x-sh", "sh");
		addMimeType("text/x-tcl", "tcl", "tk");
		addMimeType("text/x-tex", "tex", "ltx", "sty", "cls");
		addMimeType("text/x-vcalendar", "vcs");
		addMimeType("text/x-vcard", "vcf");
		addMimeType("video/bmpeg");
		addMimeType("video/bt656");
		addMimeType("video/celb");
		addMimeType("video/dl", "dl");
		addMimeType("video/dv", "dif", "dv");
		addMimeType("video/fli", "fli");
		addMimeType("video/gl", "gl");
		addMimeType("video/jpeg");
		addMimeType("video/h261");
		addMimeType("video/h263");
		addMimeType("video/h263-1998");
		addMimeType("video/h263-2000");
		addMimeType("video/mp1s");
		addMimeType("video/mp2p");
		addMimeType("video/mp2t");
		addMimeType("video/mp4", "mp4");
		addMimeType("video/mp4v-es");
		addMimeType("video/mpeg", "mpeg", "mpg", "mpe");
		addMimeType("video/mpv");
		addMimeType("video/nv");
		addMimeType("video/parityfec");
		addMimeType("video/pointer");
		addMimeType("video/quicktime", "qt", "mov");
		addMimeType("video/vnd.fvt");
		addMimeType("video/vnd.motorola.video");
		addMimeType("video/vnd.motorola.videop");
		addMimeType("video/vnd.mpegurl", "mxu");
		addMimeType("video/vnd.mts");
		addMimeType("video/vnd.nokia.interleaved-multimedia");
		addMimeType("video/vnd.vivo");
		addMimeType("video/x-la-asf", "lsf", "lsx");
		addMimeType("video/x-mng", "mng");
		addMimeType("video/x-ms-asf", "asf", "asx");
		addMimeType("video/x-ms-wm", "wm");
		addMimeType("video/x-ms-wmv", "wmv");
		addMimeType("video/x-ms-wmx", "wmx");
		addMimeType("video/x-ms-wvx", "wvx");
		addMimeType("video/x-msvideo", "avi");
		addMimeType("video/x-sgi-movie", "movie");
		addMimeType("video/x-flv", "flv");
		addMimeType("x-conference/x-cooltalk", "ice");
		addMimeType("x-world/x-vrml", "vrm", "vrml", "wrl");
	}

	/**
	 * Returns a list of all known MIME types.
	 *
	 * @return All known MIME types
	 */
	public static List<String> getAllMimeTypes() {
		return new ArrayList<String>(mimeTypes);
	}

	/**
	 * Returns a list of MIME types that are registered for the given extension.
	 *
	 * @param extension
	 *            The extension to get the MIME types for
	 * @return A list of MIME types, or an empty list if there are no registered
	 *         MIME types for the extension
	 */
	public static List<String> getMimeTypes(String extension) {
		if (extensionMimeTypes.containsKey(extension)) {
			return extensionMimeTypes.get(extension);
		}
		return Collections.emptyList();
	}

	/**
	 * Returns a default MIME type for the given extension. If the extension
	 * does not match a MIME type the default MIME typ
	 * “application/octet-stream” is returned.
	 *
	 * @param extension
	 *            The extension to get the MIME type for
	 * @return The MIME type for the extension, or the default MIME type
	 *         “application/octet-stream”
	 */
	public static String getMimeType(String extension) {
		if (extensionMimeTypes.containsKey(extension)) {
			return extensionMimeTypes.get(extension).get(0);
		}
		return DEFAULT_CONTENT_TYPE;
	}

	//
	// PRIVATE METHODS
	//

	/**
	 * Adds a MIME type and optional extensions.
	 *
	 * @param mimeType
	 *            The MIME type to add
	 * @param extensions
	 *            The extension the MIME type is registered for
	 */
	private static void addMimeType(String mimeType, String... extensions) {
		mimeTypes.add(mimeType);
		for (String extension : extensions) {
			if (!mimeTypeExtensions.containsKey(mimeType)) {
				mimeTypeExtensions.put(mimeType, new ArrayList<String>());
			}
			mimeTypeExtensions.get(mimeType).add(extension);
			if (!extensionMimeTypes.containsKey(extension)) {
				extensionMimeTypes.put(extension, new ArrayList<String>());
			}
			extensionMimeTypes.get(extension).add(mimeType);
		}
	}

}
