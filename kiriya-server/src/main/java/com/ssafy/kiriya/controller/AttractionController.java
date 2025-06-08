package com.ssafy.kiriya.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.kiriya.controller.helper.ControllerHelper;
import com.ssafy.kiriya.model.dto.Attraction;
import com.ssafy.kiriya.model.dto.Filter;
import com.ssafy.kiriya.model.dto.Member;
import com.ssafy.kiriya.model.service.attraction.AttractionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/attractions")
@RequiredArgsConstructor
public class AttractionController implements ControllerHelper {
	private final AttractionService attractionService;
	
	@GetMapping("/search")
	private ResponseEntity<?> getAttractionsByFilter(@RequestParam(required=false) String sidoCode, @RequestParam(required=false) String gunguCode, 
														@RequestParam(required=false) List<String> contentTypeList, @RequestParam(required=false) String keyword) {
		List<Attraction> attractionList = null;
		Filter filter = Filter.builder()
							.sidoCode(sidoCode).gunguCode(gunguCode).contentTypeList(contentTypeList)
							.keyword(keyword).build();
		attractionList = attractionService.getByFilter(filter);
		log.debug("filter: {}", filter);
		log.debug("attractionList size: {}", attractionList.size());
		return handleSuccess(Map.of("attractionList",attractionList));
	}
	
	/** 좋아요까지 같이 받기 **/
	@GetMapping("/search/{mno}")
	private ResponseEntity<?> getAttractionsByFilterWithLiked(@RequestParam(required=false) String sidoCode, @RequestParam(required=false) String gunguCode, 
			@RequestParam(required=false) List<String> contentTypeList, @RequestParam(required=false) String keyword,
			@PathVariable Integer mno) {
		List<Attraction> attractionList = null;
		Filter filter = Filter.builder()
				.sidoCode(sidoCode).gunguCode(gunguCode).contentTypeList(contentTypeList)
				.keyword(keyword).build();
		attractionList = attractionService.getByFilter(filter);
		for (Attraction attraction : attractionList) {
			attraction.setLiked(attractionService.getLike(attraction.getContentId(), mno));
		}
		log.debug("attractionList: {}", attractionList);
		log.debug("filter: {}", filter);
		log.debug("attractionList size: {}", attractionList.size());
		return handleSuccess(Map.of("attractionList",attractionList));
	}
	
	@GetMapping("/{contentId}/{mno}")
	private ResponseEntity<?> getByContentIdTmp(@PathVariable Integer contentId, @PathVariable Integer mno) { 
		log.debug("contentId: {}", contentId);
		Attraction attractionDetail = attractionService.getByContentId(contentId);
		attractionDetail.setLiked(attractionService.getLike(contentId, mno));
		log.debug("attractionDetail: {}", attractionDetail);
		return handleSuccess(Map.of("attractionDetail",attractionDetail));
	}
	
	@GetMapping("/{contentId}")
	private ResponseEntity<?> getByContentId(@PathVariable Integer contentId,
											@AuthenticationPrincipal Member member) {
		log.debug("contentId: {}", contentId);
		Attraction attractionDetail = attractionService.getByContentId(contentId);
		attractionDetail.setLiked(attractionService.getLike(contentId, member.getMno()));
		log.debug("attractionDetail: {}", attractionDetail);
		return handleSuccess(Map.of("attractionDetail", attractionDetail));
	}

//    @PostMapping("/api/attractions/{ano}/likes")
//	private ResponseEntity<?> createAttractionLike(@PathVariable Integer ano,
//											@AuthenticationPrincipal Member member) {
//	    return handleSuccess(Map.of("likesCnt", attractionService.increaseLike(ano, member.getMno())));
//	}
//    
//    @PostMapping("/api/attractions/{ano}/likes/{mno}")
//    private ResponseEntity<?> createAttractionLikeTmp(@PathVariable Integer ano, @PathVariable Integer mno) {
//    	return handleSuccess(Map.of("likesCnt", attractionService.increaseLike(ano, mno)));
//    }
//    
//    @DeleteMapping("/api/attractions/{ano}/likes")
//	private ResponseEntity<?> deleteAttractionLike(@PathVariable Integer ano,
//											@AuthenticationPrincipal Member member) {
//	    return handleSuccess(Map.of("likesCnt", attractionService.decreaseLike(ano, member.getMno())));
//	}
//    
//    @DeleteMapping("/api/attractions/{ano}/likes/{mno}")
//    private ResponseEntity<?> deleteAttractionLikeTmp(@PathVariable Integer ano, @PathVariable Integer mno) {
//    	return handleSuccess(Map.of("likesCnt", attractionService.decreaseLike(ano, mno)));
//    }
	
	/** contentId 기반으로 변경 **/
    @PostMapping("/{contentId}/likes")
    private ResponseEntity<?> createAttractionLikeByContentId(@PathVariable Integer contentId,
    		@AuthenticationPrincipal Member member) {
    	int ano = attractionService.getByContentId(contentId).getNo();
		return handleSuccess(Map.of("likesCnt", attractionService.increaseLike(ano, member.getMno())));
    }
    
    @PostMapping("/{contentId}/likes/{mno}")
    private ResponseEntity<?> createAttractionLikeByContentIdTmp(@PathVariable Integer contentId, @PathVariable Integer mno) {
    	int ano = attractionService.getByContentId(contentId).getNo();
    	return handleSuccess(Map.of("likesCnt", attractionService.increaseLike(ano, mno)));
    }
    
    @DeleteMapping("/{contentId}/likes")
    private ResponseEntity<?> deleteAttractionLikeByContentId(@PathVariable Integer contentId,
    		@AuthenticationPrincipal Member member) {
    	int ano = attractionService.getByContentId(contentId).getNo();
    	return handleSuccess(Map.of("likesCnt", attractionService.decreaseLike(ano, member.getMno())));
    }
    
    @DeleteMapping("/{contentId}/likes/{mno}")
    private ResponseEntity<?> deleteAttractionLikeByContentIdTmp(@PathVariable Integer contentId, @PathVariable Integer mno) {
    	int ano = attractionService.getByContentId(contentId).getNo();
    	return handleSuccess(Map.of("likesCnt", attractionService.decreaseLike(ano, mno)));
    }

}
