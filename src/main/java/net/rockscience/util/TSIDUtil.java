package net.rockscience.util;

import org.apache.commons.lang3.StringUtils;

import io.hypersistence.tsid.TSID;

public class TSIDUtil {
	public static TSID.Factory TSID_FACTORY;
 
	static {
		 TSID_FACTORY = getTsidFactory(2);
	}

	private TSIDUtil() {
	}

	public static TSID randomTsid() {
		 return TSID_FACTORY.generate();
	}

	public static Long toLong(String stringId) {
		return StringUtils.isEmpty(stringId) ? null : TSID.from(stringId).toLong();
	}

	public static String idToString(Long id) {
		return id == null ? null : TSID.from(id).toString();
	}

	public static long toLongOrZero(TSID id) {
		return id == null ? 0 : id.toLong();
	}

	public static String toStringOrNull(TSID id) {
		return id == null ? null : id.toString();
	}

	private static TSID.Factory getTsidFactory(int nodeCount) {
		 int nodeBits = (int) (Math.log(nodeCount) / Math.log(2));

		 return TSID.Factory.builder()
			  .withRandomFunction(
					TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION
			  )
			  .withNodeBits(nodeBits)
			  .build();
	}

	private static TSID.Factory getTsidFactory(int nodeCount, int nodeId) {
		 int nodeBits = (int) (Math.log(nodeCount) / Math.log(2));

		 return TSID.Factory.builder()
			  .withRandomFunction(TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION)
			  .withNodeBits(nodeBits)
			  .withNode(nodeId)
			  .build();
	}


}
