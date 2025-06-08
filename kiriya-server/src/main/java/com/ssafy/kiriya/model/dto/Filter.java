package com.ssafy.kiriya.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Filter {
//	String sidoCode;
//	String gunguCode;
//	String contentTypeId;
//	String keyword;
//}
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filter {
    String sidoCode;
    String gunguCode;
    List<String> contentTypeList;
    String keyword;
}