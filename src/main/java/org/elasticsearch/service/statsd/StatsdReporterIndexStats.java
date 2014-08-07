package org.elasticsearch.service.statsd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.indices.NodeIndicesStats;
import org.elasticsearch.index.cache.filter.FilterCacheStats;
import org.elasticsearch.index.cache.id.IdCacheStats;
import org.elasticsearch.index.flush.FlushStats;
import org.elasticsearch.index.get.GetStats;
import org.elasticsearch.index.indexing.IndexingStats;
import org.elasticsearch.index.refresh.RefreshStats;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.index.shard.DocsStats;
import org.elasticsearch.index.store.StoreStats;
import org.elasticsearch.index.fielddata.FieldDataStats;
import org.elasticsearch.index.merge.MergeStats;
import org.elasticsearch.index.percolator.stats.PercolateStats;
import org.elasticsearch.search.suggest.completion.CompletionStats;
import org.elasticsearch.index.engine.SegmentsStats;

public abstract class StatsdReporterIndexStats extends StatsdReporter {

	protected void sendDocsStats(String name, DocsStats docsStats) {
		if (null == docsStats) return;
		this.sendCount(name, "count", docsStats.getCount());
		this.sendCount(name, "deleted", docsStats.getDeleted());
	}

	protected void sendStoreStats(String name, StoreStats storeStats) {
		if (null == storeStats) return;
		this.sendCount(name, "size_in_bytes", storeStats.sizeInBytes());
		this.sendCount(name, "throttle_time_in_millis", storeStats.getThrottleTime().millis());
	}

	protected void sendIndexingStats(String type, IndexingStats indexingStats) {
		if (null == indexingStats) return;
		IndexingStats.Stats totalStats = indexingStats.getTotal();
		this.sendIndexingStatsStats(type, totalStats);

		// TODO: Maybe print out stats to shards level?
	}

	protected void sendGetStats(String type, GetStats getStats) {
		if (null == getStats) return;
		this.sendCount(type, "total", getStats.getCount());
		this.sendCount(type, "time_in_millis", getStats.getTimeInMillis());
		this.sendCount(type, "exists_total", getStats.getExistsCount());
		this.sendCount(type, "exists_time_in_millis", getStats.getExistsTimeInMillis());
		this.sendCount(type, "missing_total", getStats.getMissingCount());
		this.sendCount(type, "missing_time_in_millis", getStats.getMissingTimeInMillis());
		this.sendGauge(type, "current", getStats.current());
	}

	protected void sendSearchStats(String type, SearchStats searchStats) {
		if (null == searchStats) return;
		SearchStats.Stats totalSearchStats = searchStats.getTotal();
		this.sendSearchStatsStats(type, totalSearchStats);

		// TODO: Maybe print out stats to shards level?
	}

	protected void sendMergeStats(String type, MergeStats mergeStats) {
		if (null == mergeStats) return;
		this.sendGauge(type, "current", mergeStats.getCurrent());
		this.sendGauge(type, "current_docs", mergeStats.getCurrentNumDocs());
		this.sendGauge(type, "current_size_in_bytes", mergeStats.getCurrentSizeInBytes());
		this.sendCount(type, "total", mergeStats.getTotal());
		this.sendCount(type, "total_time_in_millis", mergeStats.getTotalTimeInMillis());
		this.sendCount(type, "total_docs", mergeStats.getTotalNumDocs());
		this.sendCount(type, "total_size_in_bytes", mergeStats.getTotalSizeInBytes());
	}

	protected void sendRefreshStats(String type, RefreshStats refreshStats) {
		if (null == refreshStats) return;
		this.sendCount(type, "total", refreshStats.getTotal());
		this.sendCount(type, "total_time_in_millis", refreshStats.getTotalTimeInMillis());
	}

	protected void sendFlushStats(String type, FlushStats flushStats) {
		if (null == flushStats) return;
		this.sendCount(type, "total", flushStats.getTotal());
		this.sendCount(type, "total_time_in_millis", flushStats.getTotalTimeInMillis());
	}

	protected void sendFilterCacheStats(String name, FilterCacheStats filterCacheStats) {
		if (null == filterCacheStats) return;
		this.sendGauge(name, "memory_size_in_bytes", filterCacheStats.getMemorySizeInBytes());
		this.sendGauge(name, "evictions", filterCacheStats.getEvictions());
	}

	protected void sendIdCacheStats(String name, IdCacheStats idCacheStats) {
		if (null == idCacheStats) return;
		this.sendGauge(name, "memory_size_in_bytes", idCacheStats.getMemorySizeInBytes());
	}

	protected void sendFielddataCacheStats(String name, FieldDataStats fielddataStats) {
		if (null == fielddataStats) return;
		this.sendGauge(name, "memory_size_in_bytes", fielddataStats.getMemorySizeInBytes());
		this.sendGauge(name, "evictions", fielddataStats.getEvictions());
	}

	protected void sendPercolateStats(String name, PercolateStats percolateStats) {
		if (null == percolateStats) return;
		this.sendCount(name, "total", percolateStats.getCount());
		this.sendCount(name, "time_in_millis", percolateStats.getTimeInMillis());
		this.sendGauge(name, "current", percolateStats.getCurrent());
		this.sendCount(name, "queries", percolateStats.getNumQueries());

		if (percolateStats.getMemorySizeInBytes() != -1)
			this.sendGauge(name, "memory_size_in_bytes", percolateStats.getMemorySizeInBytes());
	}

	protected void sendCompletionStats(String name, CompletionStats completionStats) {
		if (null == completionStats) return;
		this.sendGauge(name, "size_in_bytes", completionStats.getSizeInBytes());
	}

	protected void sendSegmentsStats(String name, SegmentsStats segmentsStats) {
		if (null == segmentsStats) return;
		this.sendGauge(name, "count", segmentsStats.getCount());
		this.sendGauge(name, "memory_in_bytes", segmentsStats.getMemoryInBytes());
	}

	protected void sendIndexingStatsStats(String type, IndexingStats.Stats indexingStatsStats) {
		if (null == indexingStatsStats) return;
		this.sendCount(type, "index_total", indexingStatsStats.getIndexCount());
		this.sendCount(type, "index_time_in_millis", indexingStatsStats.getIndexTimeInMillis());
		this.sendGauge(type, "index_current", indexingStatsStats.getIndexCount());
		this.sendCount(type, "delete_total", indexingStatsStats.getDeleteCount());
		this.sendCount(type, "delete_time_in_millis", indexingStatsStats.getDeleteTimeInMillis());
		this.sendGauge(type, "delete_current", indexingStatsStats.getDeleteCurrent());
	}

	protected void sendSearchStatsStats(String type, SearchStats.Stats searchStatsStats) {
		if (null == searchStatsStats) return;
		this.sendCount(type, "query_total", searchStatsStats.getQueryCount());
		this.sendCount(type, "query_time_in_millis", searchStatsStats.getQueryTimeInMillis());
		this.sendGauge(type, "query_current", searchStatsStats.getQueryCurrent());
		this.sendCount(type, "fetch_total", searchStatsStats.getFetchCount());
		this.sendCount(type, "fetch_time_in_millis", searchStatsStats.getFetchTimeInMillis());
		this.sendGauge(type, "fetch_current", searchStatsStats.getFetchCurrent());
	}
}
