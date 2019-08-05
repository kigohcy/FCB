package com.hitrust.bank.telegram.res.rule;

import com.hitrust.telegram.HostResponseInfo;

public interface HostMultiResponseMergeRule {
	public HostResponseInfo process(HostResponseInfo mergeInfo);
}
