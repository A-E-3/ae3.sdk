package ru.myx.geo;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.ObjectSource;
import ru.myx.ae3.flow.ObjectTarget;
import ru.myx.ae3.produce.ObjectFactory;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.serve.ServeRequest;

/*
 * Created on 09.02.2005
 */
final class FactoryFilter implements ObjectFactory<ServeRequest, ServeRequest> {
	
	private static final Class<?>[] TARGETS = {
			ServeRequest.class
	};

	private static final Class<?>[] SOURCES = {
			ServeRequest.class
	};

	private static final String[] VARIETY = {
			"GEO"
	};

	@Override
	public final boolean accepts(final String variant, final BaseObject attributes, final Class<?> source) {
		
		return true;
	}

	@Override
	public final ObjectTarget<ServeRequest> wrapTarget(final String variant, final BaseObject attributes, final Class<?> source, final ObjectTarget<ServeRequest> target) {
		
		return new TargetNormal(target);
	}

	@Override
	public final ObjectSource<ServeRequest> wrapSource(final String variant, final BaseObject attributes, final ServeRequest query) {
		
		query.setAttribute("Geo-Mean", IpGeography.getCountryCode(query.getSourceAddress(), "--", "**"));
		query.setAttribute("Geo-Peer", IpGeography.getCountryCode(query.getSourceAddressExact(), "--", "**"));
		if (Report.MODE_DEBUG) {
			query.addAttribute("X-Debug-Via", "GEO-PR");
		}
		return new ObjectSource<>() {
			
			@Override
			public ServeRequest next() {
				
				return query;
			}
			
			@Override
			public String toString() {

				return "[object GeoSource]";
			}
		};
	}

	@Override
	public final ServeRequest produce(final String variant, final BaseObject attributes, final ServeRequest query) {
		
		query.setAttribute("Geo-Mean", IpGeography.getCountryCode(query.getSourceAddress(), "--", "**"));
		query.setAttribute("Geo-Peer", IpGeography.getCountryCode(query.getSourceAddressExact(), "--", "**"));
		if (Report.MODE_DEBUG) {
			query.addAttribute("X-Debug-Via", "GEO-P1");
		}
		return query;
	}

	@Override
	public final Class<?>[] sources() {
		
		return FactoryFilter.SOURCES;
	}

	@Override
	public final Class<?>[] targets() {
		
		return FactoryFilter.TARGETS;
	}

	@Override
	public final String[] variety() {
		
		return FactoryFilter.VARIETY;
	}
}
