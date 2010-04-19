package org.drugis.mtc.yadas

import org.drugis.mtc._
import gov.lanl.yadas.ArgumentMaker

/**
 * ArgumentMaker for individual treatment success probabilities within studies.
 * p_i,k = ilogit(theta_i,k) ; theta_i,k = mu_i + delta_i,b(i),k
 */
class SuccessProbabilityArgumentMaker(
		override val model: NetworkModel[DichotomousMeasurement],
		override val sIdx: Int,
		override val dIdx: Int)
extends ArgumentMaker with ThetaMaker[DichotomousMeasurement] {
	/**
	 * Calculate "the argument": an array of succes-probabilities, one for
	 * each study-arm.
	 * data[sIdx] should contain study baseline means, one per study
	 * data[dIdx] should contain relative effects, one for each non-baseline
	 */
	def getArgument(data: Array[Array[Double]]): Array[Double] = {
		Array.make(0, 0.0) ++ {
			for {d <- model.data} yield prob(d._1, d._2.treatment, data)
		}
	}

	private def ilogit(x: Double): Double = 1 / (1 + Math.exp(-x))

	private def prob(s: Study[DichotomousMeasurement],
			t: Treatment, data: Array[Array[Double]])
	: Double = {
		ilogit(theta(s, t, data))
	}
}