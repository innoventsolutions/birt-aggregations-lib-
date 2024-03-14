package innovent.birt.test;

import java.math.BigDecimal;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Assert;
import org.junit.Test;

import innovent.birt.aggregations.DecimalLast;

public class DecimalLastTest {
	private Accumulator prepare() {
		final IAggrFunction fn = new DecimalLast();
		Assert.assertEquals("getType() must return " + IAggrFunction.SUMMARY_AGGR, fn.getType(),
			IAggrFunction.SUMMARY_AGGR);
		Assert.assertEquals("getDataType() must return " + DataType.DECIMAL_TYPE, fn.getDataType(),
			DataType.DECIMAL_TYPE);
		final var parameterDefns = fn.getParameterDefn();
		Assert.assertNotNull(parameterDefns);
		Assert.assertEquals(parameterDefns.length, 1);
		final var accumulator = fn.newAccumulator();
		Assert.assertNotNull(accumulator);
		return accumulator;
	}

	@Test
	public void testAccumulateBigDecimalZero() {
		final var accumulator = prepare();
		try {
			accumulator.start();
			accumulator.onRow(new Object[] { new BigDecimal(0) });
			accumulator.onRow(new Object[] { new BigDecimal(0) });
			accumulator.onRow(new Object[] { new BigDecimal(0) });
			accumulator.onRow(new Object[] { new BigDecimal(0) });
			accumulator.onRow(new Object[] { new BigDecimal(0) });
			accumulator.finish();
			final var value = accumulator.getValue();
			Assert.assertEquals(value, new BigDecimal(0));
		}
		catch (final DataException e) {
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testAccumulateBigDecimal() {
		final var accumulator = prepare();
		try {
			accumulator.start();
			accumulator.onRow(new Object[] { new BigDecimal(1) });
			accumulator.onRow(new Object[] { new BigDecimal(1) });
			accumulator.onRow(new Object[] { new BigDecimal(2) });
			accumulator.onRow(new Object[] { new BigDecimal(3) });
			accumulator.onRow(new Object[] { new BigDecimal(3) });
			accumulator.finish();
			final var value = accumulator.getValue();
			Assert.assertEquals(value, new BigDecimal(3));
		}
		catch (final DataException e) {
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testAccumulateIntegers() {
		final var accumulator = prepare();
		try {
			accumulator.start();
			accumulator.onRow(new Object[] { Integer.valueOf(1) });
			accumulator.onRow(new Object[] { Integer.valueOf(1) });
			accumulator.onRow(new Object[] { Integer.valueOf(2) });
			accumulator.onRow(new Object[] { Integer.valueOf(3) });
			accumulator.onRow(new Object[] { Integer.valueOf(3) });
			accumulator.finish();
			final var value = accumulator.getValue();
			Assert.assertEquals(value, Integer.valueOf(3));
		}
		catch (final DataException e) {
			Assert.fail(e.toString());
		}
	}

	@Test
	public void testAccumulateDoubles() {
		final var accumulator = prepare();
		try {
			accumulator.start();
			accumulator.onRow(new Object[] { Double.valueOf(1) });
			accumulator.onRow(new Object[] { Double.valueOf(1) });
			accumulator.onRow(new Object[] { Double.valueOf(2) });
			accumulator.onRow(new Object[] { Double.valueOf(3) });
			accumulator.onRow(new Object[] { Double.valueOf(3) });
			accumulator.finish();
			final var value = accumulator.getValue();
			final var expected = Double.valueOf(3);
			Assert.assertEquals(value, expected);
		}
		catch (final DataException e) {
			Assert.fail(e.toString());
		}
	}
}