package mujinjang.couponsystem.common.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringToByteBufferUtils {

	public static ByteBuffer toByteBuffer(String... string) {
		final StringBuilder sb = new StringBuilder();
		for (String s : string) {
			sb.append(s).append("\n");
		}
		return StandardCharsets.UTF_8.encode(sb.substring(0, sb.length() - 1));
	}
}
