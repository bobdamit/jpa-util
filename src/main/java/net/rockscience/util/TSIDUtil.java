package net.rockscience.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.hypersistence.tsid.TSID;

public class TSIDUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TSIDUtil.class);
	public static TSID.Factory TSID_FACTORY;
 
	static {
		 TSID_FACTORY = getTsidFactory(2);
		 LOGGER.debug("Initialized TSID factory with node count: 2");
	}

	private TSIDUtil() {
	}

	public static TSID randomTsid() {
		 return TSID_FACTORY.generate();
	}

	public static Long toLong(String stringId) {
		if (StringUtils.isEmpty(stringId)) {
			return null;
		}
		try {
			return TSID.from(stringId).toLong();
		} catch (Exception e) {
			LOGGER.error("Failed to convert string to TSID: {}", stringId, e);
			throw e;
		}
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

	public static TSID fromString(String stringId) {
		if (StringUtils.isEmpty(stringId)) {
			return null;
		}
		try {
			return TSID.from(stringId);
		} catch (Exception e) {
			LOGGER.error("Failed to parse TSID from string: {}", stringId, e);
			throw e;
		}
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

}
