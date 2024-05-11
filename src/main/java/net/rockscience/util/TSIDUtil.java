package net.rockscience.util;

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

	public static TSID.Factory getTsidFactory(int nodeCount) {
		 int nodeBits = (int) (Math.log(nodeCount) / Math.log(2));

		 return TSID.Factory.builder()
			  .withRandomFunction(
					TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION
			  )
			  .withNodeBits(nodeBits)
			  .build();
	}

	public static TSID.Factory getTsidFactory(int nodeCount, int nodeId) {
		 int nodeBits = (int) (Math.log(nodeCount) / Math.log(2));

		 return TSID.Factory.builder()
			  .withRandomFunction(TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION)
			  .withNodeBits(nodeBits)
			  .withNode(nodeId)
			  .build();
	}
}
