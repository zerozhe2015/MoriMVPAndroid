package com.moriarty.base.http;

import java.util.Map;

public interface CommonRequestParamsProvider {

    Map<String, String> provideCommonRequestParams();
}
