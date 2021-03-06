/*
 * This file is part of the GeMTC software for MTC model generation and
 * analysis. GeMTC is distributed from http://drugis.org/gemtc.
 * Copyright (C) 2009-2012 Gert van Valkenhoef.
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

package org.drugis.mtc.parameterization;

import java.util.List;
import java.util.Map;

import org.drugis.mtc.model.Treatment;

public class InconsistencyStartingValueGenerator {
    /**
     * Derive a starting value for the InconsistencyParameter based on the
     * values assigned to the BasicParameters.
     * @param w The InconsistencyParameter.
     * @param pmtz The parameterization in which p occurs.
     * @param generator A StartingValueGenerator to be used to close the cycle (one relative effect will be missing).
     * @param basicValues Starting values assigned to the basic parameters.
     * @return A starting value for the inconsistency parameter. 
     */
	public static double generate(InconsistencyParameter w, InconsistencyParameterization pmtz,
			StartingValueGenerator generator, Map<BasicParameter, Double> basicValues) {
		double x = 0.0;
		final List<Treatment> c = w.getCycle();
		for (int i = 1; i < c.size(); ++i) {
			BasicParameter p = new BasicParameter(c.get(i - 1), c.get(i));
			BasicParameter q = new BasicParameter(c.get(i), c.get(i - 1));
			if (basicValues.containsKey(p)) {
				x += basicValues.get(p);
			} else if (basicValues.containsKey(q)) {
				x -= basicValues.get(q);
			} else {
				x += generator.getRelativeEffect(p);
			}
		}
		return x;
	}
}
