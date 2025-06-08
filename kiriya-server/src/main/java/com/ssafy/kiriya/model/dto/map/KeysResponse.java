package com.ssafy.kiriya.model.dto.map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeysResponse {
	String keyVworld;
	String keySgisServiceId;
	String keySgisSecurity;
	String keyData;
}
