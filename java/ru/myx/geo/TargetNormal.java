/**
 *
 */
package ru.myx.geo;

import ru.myx.ae3.flow.ObjectTarget;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.serve.ServeRequest;

final class TargetNormal implements ObjectTarget<ServeRequest> {

	final ObjectTarget<ServeRequest> target;

	TargetNormal(final ObjectTarget<ServeRequest> target) {

		this.target = target;
	}

	@Override
	public final boolean absorb(final ServeRequest query) {

		query.setAttribute("Geo-Mean", IpGeography.getCountryCode(query.getSourceAddress(), "--", "**"));
		query.setAttribute("Geo-Peer", IpGeography.getCountryCode(query.getSourceAddressExact(), "--", "**"));
		if (Report.MODE_DEBUG) {
			query.addAttribute("X-Debug-Via", "GEO-TA");
		}
		return this.target.absorb(query);
	}

	@Override
	public final Class<? extends ServeRequest> accepts() {

		return ServeRequest.class;
	}

	@Override
	public final String toString() {

		return "PROFLT-GEO target";
	}
}
