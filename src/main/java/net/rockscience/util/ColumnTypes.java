package net.rockscience.util;

public interface ColumnTypes {
	String BOOLEAN = "tinyint unsigned";
	String ID_256 = "tinyint unsigned";
	String ID_64K = "smallint unsigned";
	String ID_YUGE = "bigint unsigned";

	/** -128...127 */
	String ENUM_256_SIGNED = "tinyint";
	/** 0...255 */
	String ENUM_256 = "tinyint unsigned";
	/** 0...64K */
	String ENUM_64K = "smallint unsigned";

	String INT_256 = "tinyint unsigned";

	/** 0..1.000 */
	String Float01P3 = "decimal(5,3)";
	/** 0...+/-999.99 */
	String MONEY_1K = "decimal(5,2)";
	/** 0...+/-9,999.99 */
	String MONEY_10K = "decimal(6,2)";
	/** 0...+/-99,999.99 */
	String MONEY_100K = "decimal(7,2)";
	/** 0...+/-999,999.99 */
	String MNONEY_1M = "decimal(8,2)";
}
