package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.sf.juoserver.api.Encoder;

/**
 * {@link Encoder} which compresses bytes using the Huffman algorithm.
 * @see <a href="http://en.wikipedia.org/wiki/Huffman_coding">Wikipedia</a>
 */
public class Huffman implements Encoder {
	/**
	 * Binary tree for the Huffman encoding.
	 * <p/>
	 * The i-th element is an array:
	 * <ol>
	 * <li>whose second element is the encoding
	 * for i - if translated into binary notation,
	 * this number can be seen as the path within
	 * the tree to reach the "leaf" i from the root
	 * of the tree (e.g. where 0 is the left branch
	 * and 1 is the right branch)</li>
	 * <li>and the first element is the number
	 * of significant bits within the path.</li>
	 * </ol>
	 * Take the number 5, for example:
	 * <pre><tt>encodeTree[ 5 ] == {0x06, 0x28}</tt></pre>
	 * therefore:
	 * <ul>
	 * <li>5 is encoded as 0x28 (= 101000 in binary notation),</li>
	 * <li>which has (0x06 =) 6 significant bits.</li>
	 * </ul>
	 * This can be visualized like this:
	 * <pre>
	 *             root
	 *               \ (1)
	 *           (0) /
	 *               \ (1)
	 *           (0) /
	 *          (0) /
	 *         (0) /
	 *           (*) <-- Here 'lies' number 5 in the encoding tree.    
	 * </pre>
	 * Numbers with shorter paths (i.e. with a lower number
	 * of significant bits) are those who are statistically
	 * less likely to appear (less probable), hence the algorithm
	 * ensures that we use more space only a "few times", thus
	 * saving space (bytes).
	 */
	static final short[][] encodeTree = new short[][] {
		{0x02, 0x00}, 	{0x05, 0x1F}, 	{0x06, 0x22}, 	{0x07, 0x34}, 	{0x07, 0x75}, 	{0x06, 0x28}, 	{0x06, 0x3B}, 	{0x07, 0x32}, 
		{0x08, 0xE0}, 	{0x08, 0x62}, 	{0x07, 0x56}, 	{0x08, 0x79}, 	{0x09, 0x19D},	{0x08, 0x97}, 	{0x06, 0x2A}, 	{0x07, 0x57}, 
		{0x08, 0x71}, 	{0x08, 0x5B}, 	{0x09, 0x1CC},	{0x08, 0xA7}, 	{0x07, 0x25}, 	{0x07, 0x4F}, 	{0x08, 0x66}, 	{0x08, 0x7D}, 
		{0x09, 0x191},	{0x09, 0x1CE}, 	{0x07, 0x3F}, 	{0x09, 0x90}, 	{0x08, 0x59}, 	{0x08, 0x7B}, 	{0x08, 0x91}, 	{0x08, 0xC6}, 
		{0x06, 0x2D}, 	{0x09, 0x186}, 	{0x08, 0x6F}, 	{0x09, 0x93}, 	{0x0A, 0x1CC},	{0x08, 0x5A}, 	{0x0A, 0x1AE},	{0x0A, 0x1C0}, 
		{0x09, 0x148},	{0x09, 0x14A}, 	{0x09, 0x82}, 	{0x0A, 0x19F}, 	{0x09, 0x171},	{0x09, 0x120}, 	{0x09, 0xE7}, 	{0x0A, 0x1F3}, 
		{0x09, 0x14B},	{0x09, 0x100},	{0x09, 0x190},	{0x06, 0x13}, 	{0x09, 0x161},	{0x09, 0x125},	{0x09, 0x133},	{0x09, 0x195}, 
		{0x09, 0x173},	{0x09, 0x1CA},	{0x09, 0x86}, 	{0x09, 0x1E9}, 	{0x09, 0xDB}, 	{0x09, 0x1EC},	{0x09, 0x8B}, 	{0x09, 0x85}, 
		{0x05, 0x0A}, 	{0x08, 0x96}, 	{0x08, 0x9C}, 	{0x09, 0x1C3}, 	{0x09, 0x19C},	{0x09, 0x8F}, 	{0x09, 0x18F},	{0x09, 0x91}, 
		{0x09, 0x87}, 	{0x09, 0xC6}, 	{0x09, 0x177},	{0x09, 0x89}, 	{0x09, 0xD6}, 	{0x09, 0x8C}, 	{0x09, 0x1EE},	{0x09, 0x1EB}, 
		{0x09, 0x84}, 	{0x09, 0x164}, 	{0x09, 0x175},	{0x09, 0x1CD}, 	{0x08, 0x5E}, 	{0x09, 0x88}, 	{0x09, 0x12B},	{0x09, 0x172}, 
		{0x09, 0x10A},	{0x09, 0x8D}, 	{0x09, 0x13A},	{0x09, 0x11C}, 	{0x0A, 0x1E1},	{0x0A, 0x1E0}, 	{0x09, 0x187},	{0x0A, 0x1DC}, 
		{0x0A, 0x1DF},	{0x07, 0x74}, 	{0x09, 0x19F},	{0x08, 0x8D},	{0x08, 0xE4}, 	{0x07, 0x79}, 	{0x09, 0xEA}, 	{0x09, 0xE1}, 
		{0x08, 0x40}, 	{0x07, 0x41}, 	{0x09, 0x10B},	{0x09, 0xB0}, 	{0x08, 0x6A}, 	{0x08, 0xC1}, 	{0x07, 0x71}, 	{0x07, 0x78}, 
		{0x08, 0xB1}, 	{0x09, 0x14C}, 	{0x07, 0x43}, 	{0x08, 0x76}, 	{0x07, 0x66}, 	{0x07, 0x4D}, 	{0x09, 0x8A}, 	{0x06, 0x2F}, 
		{0x08, 0xC9},	{0x09, 0xCE}, 	{0x09, 0x149},	{0x09, 0x160}, 	{0x0A, 0x1BA}, 	{0x0A, 0x19E}, 	{0x0A, 0x39F}, 	{0x09, 0xE5},
		{0x09, 0x194}, 	{0x09, 0x184}, 	{0x09, 0x126}, 	{0x07, 0x30}, 	{0x08, 0x6C}, 	{0x09, 0x121}, 	{0x09, 0x1E8}, 	{0x0A, 0x1C1}, 
		{0x0A, 0x11D}, 	{0x0A, 0x163}, 	{0x0A, 0x385}, 	{0x0A, 0x3DB}, 	{0x0A, 0x17D}, 	{0x0A, 0x106}, 	{0x0A, 0x397}, 	{0x0A, 0x24E}, 
		{0x07, 0x2E}, 	{0x08, 0x98}, 	{0x0A, 0x33C}, 	{0x0A, 0x32E}, 	{0x0A, 0x1E9}, 	{0x09, 0xBF}, 	{0x0A, 0x3DF}, 	{0x0A, 0x1DD}, 
		{0x0A, 0x32D}, 	{0x0A, 0x2ED}, 	{0x0A, 0x30B}, 	{0x0A, 0x107}, 	{0x0A, 0x2E8}, 	{0x0A, 0x3DE}, 	{0x0A, 0x125}, 	{0x0A, 0x1E8}, 
		{0x09, 0xE9}, 	{0x0A, 0x1CD}, 	{0x0A, 0x1B5}, 	{0x09, 0x165}, 	{0x0A, 0x232}, 	{0x0A, 0x2E1}, 	{0x0B, 0x3AE}, 	{0x0B, 0x3C6}, 
		{0x0B, 0x3E2}, 	{0x0A, 0x205}, 	{0x0A, 0x29A}, 	{0x0A, 0x248}, 	{0x0A, 0x2CD}, 	{0x0A, 0x23B}, 	{0x0B, 0x3C5}, 	{0x0A, 0x251}, 
		{0x0A, 0x2E9}, 	{0x0A, 0x252}, 	{0x09, 0x1EA}, 	{0x0B, 0x3A0}, 	{0x0B, 0x391}, 	{0x0A, 0x23C}, 	{0x0B, 0x392}, 	{0x0B, 0x3D5}, 
		{0x0A, 0x233}, 	{0x0A, 0x2CC}, 	{0x0B, 0x390}, 	{0x0A, 0x1BB}, 	{0x0B, 0x3A1}, 	{0x0B, 0x3C4}, 	{0x0A, 0x211}, 	{0x0A, 0x203}, 
		{0x09, 0x12A}, 	{0x0A, 0x231}, 	{0x0B, 0x3E0}, 	{0x0A, 0x29B}, 	{0x0B, 0x3D7}, 	{0x0A, 0x202}, 	{0x0B, 0x3AD}, 	{0x0A, 0x213}, 
		{0x0A, 0x253}, 	{0x0A, 0x32C}, 	{0x0A, 0x23D}, 	{0x0A, 0x23F}, 	{0x0A, 0x32F}, 	{0x0A, 0x11C}, 	{0x0A, 0x384}, 	{0x0A, 0x31C}, 
		{0x0A, 0x17C}, 	{0x0A, 0x30A}, 	{0x0A, 0x2E0}, 	{0x0A, 0x276}, 	{0x0A, 0x250}, 	{0x0B, 0x3E3}, 	{0x0A, 0x396}, 	{0x0A, 0x18F}, 
		{0x0A, 0x204}, 	{0x0A, 0x206}, 	{0x0A, 0x230}, 	{0x0A, 0x265}, 	{0x0A, 0x212}, 	{0x0A, 0x23E}, 	{0x0B, 0x3AC}, 	{0x0B, 0x393}, 
		{0x0B, 0x3E1}, 	{0x0A, 0x1DE}, 	{0x0B, 0x3D6}, 	{0x0A, 0x31D}, 	{0x0B, 0x3E5}, 	{0x0B, 0x3E4}, 	{0x0A, 0x207}, 	{0x0B, 0x3C7}, 
		{0x0A, 0x277}, 	{0x0B, 0x3D4}, 	{0x08, 0xC0},	{0x0A, 0x162}, 	{0x0A, 0x3DA}, 	{0x0A, 0x124}, 	{0x0A, 0x1B4}, 	{0x0A, 0x264}, 
		{0x0A, 0x33D}, 	{0x0A, 0x1D1}, 	{0x0A, 0x1AF}, 	{0x0A, 0x39E}, 	{0x0A, 0x24F}, 	{0x0B, 0x373}, 	{0x0A, 0x249}, 	{0x0B, 0x372}, 
		{0x09, 0x167}, 	{0x0A, 0x210}, 	{0x0A, 0x23A}, 	{0x0A, 0x1B8}, 	{0x0B, 0x3AF}, 	{0x0A, 0x18E}, 	{0x0A, 0x2EC}, 	{0x07, 0x62}, 
		{0x04, 0x0D}
	};

	/**
	 * Binary tree for the Huffman decoding.
	 */
	private static int[] decodeTree = {
		/* 0 */1, 2,
		/* 1 */3, 4,
		/* 2 */5, 0,
		/* 3 */6, 7,
		/* 4 */8, 9,
		/* 5 */10, 11,
		/* 6 */12, 13,
		/* 7 */-256, 14,
		/* 8 */15, 16,
		/* 9 */17, 18,
		/* 10 */19, 20,
		/* 11 */21, 22,
		/* 12 */-1, 23,
		/* 13 */24, 25,
		/* 14 */26, 27,
		/* 15 */28, 29,
		/* 16 */30, 31,
		/* 17 */32, 33,
		/* 18 */34, 35,
		/* 19 */36, 37,
		/* 20 */38, 39,
		/* 21 */40, -64,
		/* 22 */41, 42,
		/* 23 */43, 44,
		/* 24 */-6, 45,
		/* 25 */46, 47,
		/* 26 */48, 49,
		/* 27 */50, 51,
		/* 28 */-119, 52,
		/* 29 */-32, 53,
		/* 30 */54, -14,
		/* 31 */55, -5,
		/* 32 */56, 57,
		/* 33 */58, 59,
		/* 34 */60, -2,
		/* 35 */61, 62,
		/* 36 */63, 64,
		/* 37 */65, 66,
		/* 38 */67, 68,
		/* 39 */69, 70,
		/* 40 */71, 72,
		/* 41 */-51, 73,
		/* 42 */74, 75,
		/* 43 */76, 77,
		/* 44 */-101, -111,
		/* 45 */-4, -97,
		/* 46 */78, 79,
		/* 47 */-110, 80,
		/* 48 */81, -116,
		/* 49 */82, 83,
		/* 50 */84, -255,
		/* 51 */85, 86,
		/* 52 */87, 88,
		/* 53 */89, 90,
		/* 54 */-15, -10,
		/* 55 */91, 92,
		/* 56 */-21, 93,
		/* 57 */-117, 94,
		/* 58 */95, 96,
		/* 59 */97, 98,
		/* 60 */99, 100,
		/* 61 */-114, 101,
		/* 62 */-105, 102,
		/* 63 */-26, 103,
		/* 64 */104, 105,
		/* 65 */106, 107,
		/* 66 */108, 109,
		/* 67 */110, 111,
		/* 68 */112, -3,
		/* 69 */113, -7,
		/* 70 */114, -131,
		/* 71 */115, -144,
		/* 72 */116, 117,
		/* 73 */-20, 118,
		/* 74 */119, 120,
		/* 75 */121, 122,
		/* 76 */123, 124,
		/* 77 */125, 126,
		/* 78 */127, 128,
		/* 79 */129, -100,
		/* 80 */130, -8,
		/* 81 */131, 132,
		/* 82 */133, 134,
		/* 83 */-120, 135,
		/* 84 */136, -31,
		/* 85 */137, 138,
		/* 86 */-109, -234,
		/* 87 */139, 140,
		/* 88 */141, 142,
		/* 89 */143, 144,
		/* 90 */-112, 145,
		/* 91 */-19, 146,
		/* 92 */147, 148,
		/* 93 */149, -66,
		/* 94 */150, -145,
		/* 95 */-13, -65,
		/* 96 */151, 152,
		/* 97 */153, 154,
		/* 98 */-30, 155,
		/* 99 */156, 157,
		/* 100 */-99, 158,
		/* 101 */159, 160,
		/* 102 */161, 162,
		/* 103 */-23, 163,
		/* 104 */-29, 164,
		/* 105 */-11, 165,
		/* 106 */166, -115,
		/* 107 */167, 168,
		/* 108 */169, 170,
		/* 109 */-16, 171,
		/* 110 */-34, 172,
		/* 111 */173, -132,
		/* 112 */174, -108,
		/* 113 */175, -22,
		/* 114 */176, -9,
		/* 115 */177, -84,
		/* 116 */-17, -37,
		/* 117 */-28, 178,
		/* 118 */179, 180,
		/* 119 */181, 182,
		/* 120 */183, 184,
		/* 121 */185, 186,
		/* 122 */187, -104,
		/* 123 */188, -78,
		/* 124 */189, -61,
		/* 125 */-79, -178,
		/* 126 */-59, -134,
		/* 127 */190, -25,
		/* 128 */-83, -18,
		/* 129 */191, -57,
		/* 130 */-67, 192,
		/* 131 */-98, 193,
		/* 132 */-12, -68,
		/* 133 */194, 195,
		/* 134 */-55, -128,
		/* 135 */-24, -50,
		/* 136 */-70, 196,
		/* 137 */-94, -33,
		/* 138 */197, -129,
		/* 139 */-74, 198,
		/* 140 */-82, 199,
		/* 141 */-56, -87,
		/* 142 */-44, 200,
		/* 143 */-248, 201,
		/* 144 */-163, -81,
		/* 145 */-52, -123,
		/* 146 */202, -113,
		/* 147 */-48, -41,
		/* 148 */-122, -40,
		/* 149 */203, -90,
		/* 150 */-54, 204,
		/* 151 */-86, -192,
		/* 152 */205, 206,
		/* 153 */207, -130,
		/* 154 */-53, 208,
		/* 155 */-133, -45,
		/* 156 */209, 210,
		/* 157 */211, -91,
		/* 158 */212, 213,
		/* 159 */-106, -88,
		/* 160 */214, 215,
		/* 161 */216, 217,
		/* 162 */218, -49,
		/* 163 */219, 220,
		/* 164 */221, 222,
		/* 165 */223, 224,
		/* 166 */225, 226,
		/* 167 */227, -102,
		/* 168 */-160, 228,
		/* 169 */-46, 229,
		/* 170 */-127, 230,
		/* 171 */-103, 231,
		/* 172 */232, 233,
		/* 173 */-60, 234,
		/* 174 */235, -76,
		/* 175 */236, -121,
		/* 176 */237, -73,
		/* 177 */-149, 238,
		/* 178 */239, -107,
		/* 179 */-35, 240,
		/* 180 */-71, -27,
		/* 181 */-69, 241,
		/* 182 */-89, -77,
		/* 183 */-62, -118,
		/* 184 */-75, -85,
		/* 185 */-72, -58,
		/* 186 */-63, -80,
		/* 187 */242, -42,
		/* 188 */-150, -157,
		/* 189 */-139, -236,
		/* 190 */-126, -243,
		/* 191 */-142, -214,
		/* 192 */-138, -206,
		/* 193 */-240, -146,
		/* 194 */-204, -147,
		/* 195 */-152, -201,
		/* 196 */-227, -207,
		/* 197 */-154, -209,
		/* 198 */-153, -254,
		/* 199 */-176, -156,
		/* 200 */-165, -210,
		/* 201 */-172, -185,
		/* 202 */-195, -170,
		/* 203 */-232, -211,
		/* 204 */-219, -239,
		/* 205 */-200, -177,
		/* 206 */-175, -212,
		/* 207 */-244, -143,
		/* 208 */-246, -171,
		/* 209 */-203, -221,
		/* 210 */-202, -181,
		/* 211 */-173, -250,
		/* 212 */-184, -164,
		/* 213 */-193, -218,
		/* 214 */-199, -220,
		/* 215 */-190, -249,
		/* 216 */-230, -217,
		/* 217 */-169, -216,
		/* 218 */-191, -197,
		/* 219 */-47, 243,
		/* 220 */244, 245,
		/* 221 */246, 247,
		/* 222 */-148, -159,
		/* 223 */248, 249,
		/* 224 */-92, -93,
		/* 225 */-96, -225,
		/* 226 */-151, -95,
		/* 227 */250, 251,
		/* 228 */-241, 252,
		/* 229 */-161, -36,
		/* 230 */253, 254,
		/* 231 */-135, -39,
		/* 232 */-187, -124,
		/* 233 */255, -251,
		/* 234 */-162, -238,
		/* 235 */-242, -38,
		/* 236 */-43, -125,
		/* 237 */-215, -253,
		/* 238 */-140, -208,
		/* 239 */-137, -235,
		/* 240 */-158, -237,
		/* 241 */-136, -205,
		/* 242 */-155, -141,
		/* 243 */-228, -229,
		/* 244 */-213, -168,
		/* 245 */-224, -194,
		/* 246 */-196, -226,
		/* 247 */-183, -233,
		/* 248 */-231, -167,
		/* 249 */-174, -189,
		/* 250 */-252, -166,
		/* 251 */-198, -222,
		/* 252 */-188, -179,
		/* 253 */-223, -182,
		/* 254 */-180, -186,
		/* 255 */-245, -247, };
	
	private static final int MAX_PACKET_SIZE = 0x10000;
	
	@Override
	public byte[] encode(byte[] source) {
		if (source.length > MAX_PACKET_SIZE) {
			throw new IllegalArgumentException("Packet too large (max = " + MAX_PACKET_SIZE + ")");
		}
		
		byte[] retval = new byte[0];
		int nrBits = 0, cBits = 0, val = 0;
		byte current = 0;
		int length = source.length;
		
		if (source == null || length == 0) return null;

		for (int i = 0; i < length; i++) {
			nrBits = encodeTree[ source[i] & 0xFF ][0] - 1;
			val = encodeTree[ source[i] & 0xFF ][1];

			for (int n = nrBits; n >= 0; n--) {
				int x = (val >> n) % 2;
				current <<= 1;
				current += (byte) x;

				cBits++;
				if(cBits == 8) {
					retval = appendByte(current, retval);
					cBits = 0;
				}
			}
		}

		nrBits = encodeTree[256][0] - 1;
		val = encodeTree[256][1];

		for (int n = nrBits; n >= 0; n--) {
			int x = (val >> n) % 2;
			current <<= 1;
			current += (byte) x;

			cBits++;
			if (cBits == 8) {
				retval = appendByte(current, retval);
				cBits = 0;
			}
		}

		while (cBits != 0) {
			current <<= 1;
			cBits++;

			if (cBits == 8) {
				retval = appendByte(current, retval);
				cBits = 0;
			}
		}

		return retval;
	}

	/**
	 * Decodes a compressed byte array (for test purposes).
	 * @param source compressed byte array to be decoded
	 * @return the decoded byte array 
	 */
	static byte[] decode(byte[] source) {
		if (source.length == 0) {
			return new byte[0];
		}

		byte[] dest = new byte[MAX_PACKET_SIZE];
		
		int index = 0;
		int mark = 0;
		int bitNum = 8;
		int treePos = 0;
		int value = 0;
		int mask = 0;
		int len = source.length;
		int off = 0;

		while (true) {
			if (bitNum == 8) {
				// End of input
				if (len == 0) {
					return Arrays.copyOfRange(dest, 0, mark);
				}
				len--;
				value = source[off++];
				bitNum = 0;
				mask = 0x80;
			}
			if ((value & mask) != 0) {
				treePos = decodeTree[treePos * 2];
			} else {
				treePos = decodeTree[treePos * 2 + 1];
			}
			
			mask >>= 1; // Select the next bit to the right
			bitNum++;

			if (treePos <= 0) { // This is a leaf
				// Special flush character, marks end of packet
				if (treePos == -256) {
					bitNum = 8;  // Flush the rest of the byte
					treePos = 0; // Start on tree top again

					// Commit here, marks end of one uo packet
					mark = index;
					continue;
				}
				dest[index++] = ((byte) -treePos);
				treePos = 0; // Start on tree top again
			}
		}
	}
	
	/**
	 * Adds the specified byte to the destination array.
	 * @param last byte to be added
	 * @param source array to add the byte to
	 * @return a new array equal to the provided one,
	 * having the last byte appended at the end
	 */
	static byte[] appendByte(byte last, byte[] source) {
		ByteBuffer bb = ByteBuffer.allocate(source.length + 1);
		bb.put(source);
		bb.put(last);
		return bb.array();
	}
}
