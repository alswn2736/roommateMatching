<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="modalOverlay" id="writeModalOverlay" hidden>
    <section class="writeModal" role="dialog" aria-modal="true" aria-labelledby="writeModalTitle">
        
        <div class="writeModalHeader">
            <h2 id="writeModalTitle">새 메이트 모집글 작성</h2>

            <button type="button" id="closeWriteModal" class="modalCloseButton" aria-label="닫기">
                <svg xmlns="http://www.w3.org/2000/svg" width="16.668" height="16.668" viewBox="0 0 19 19" fill="none">
                    <path d="M11.8342 6.8338L9.334 9.334M9.334 9.334L6.8338 11.8342M9.334 9.334L6.8338 6.8338M9.334 9.334L11.8342 11.8342M17.668 9.334C17.668 13.9367 13.9367 17.668 9.334 17.668C4.73126 17.668 1 13.9367 1 9.334C1 4.73126 4.73126 1 9.334 1C13.9367 1 17.668 4.73126 17.668 9.334Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
                </svg>
            </button>
        </div>

        <form id="writeForm"
        	action="${pageContext.request.contextPath}/app/feed/write"
      		method="post">
      		<input type="hidden" name="postId" id="writePostId">
            
            <div class="writeField">
                <label for="recruitTitle">모집글 제목</label>
                <input type="text" id="recruitTitle" name="title" class="form-control"
                	maxlength="100" placeholder="새 학기 기숙사 같이 쓸 동갑내기 구해요 (학과, 성향 요약)"
                	value="${post.title}" required>
            </div>
            
            <div class="writeField">
                <span class="writeLabel">희망 방 유형</span>

                <div class="roomTypeOptions">
				    <label class="roomTypeOption selected">
				        <input type="radio" name="roomType"
				               value="DOUBLE" ${post.roomType == 'DOUBLE' ? 'checked' : ''}>
				        <span>기숙사 2인실</span>
				    </label>
				    <label class="roomTypeOption">
				        <input type="radio" name="roomType"
				               value="QUAD" ${post.roomType == 'QUAD' ? 'checked' : ''}>
				        <span>기숙사 4인실</span>
				    </label>
				</div>
            </div>

            
            <div class="writeField">
                <span class="writeLabel">
                    내 생활 습관 키워드 선택 (중복 가능)
                </span>

                <div class="habitTags">
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="아침형" ${post.kwSleepTime == 'M' ? 'checked' : ''}>
					    <span># 아침형(얼리버드)</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="저녁형" ${post.kwSleepTime == 'E' ? 'checked' : ''}>
					    <span># 저녁형(올빼미)</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="비흡연자" ${post.kwSmoking == 'N' ? 'checked' : ''}>
					    <span># 비흡연자</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="흡연 가능" ${post.kwSmoking == 'Y' ? 'checked' : ''}>
					    <span># 흡연 가능</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="조용한 편" ${post.kwSensitivity == 'Q' ? 'checked' : ''}>
					    <span># 조용한 편</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="사교적" ${post.kwSensitivity == 'S' ? 'checked' : ''}>
					    <span># 사교적</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="에어컨 빵빵" ${post.kwAirCon == 'Y' ? 'checked' : ''}>
					    <span># 에어컨 빵빵</span>
					</label>
					
					<label class="habitTag">
					    <input type="checkbox" name="habits" value="위생청결 철저" ${post.kwClean == 'Y' ? 'checked' : ''}>
					    <span># 위생청결 철저</span>
					</label>
                </div>
            </div>

            
            <div class="writeField">
                <label for="recruitDescription">
                    상세 모집 설명 및 요청사항
                </label>
                
                <textarea id="recruitDescription" name="description"
			          class="form-control"
			          rows="5"
			          maxlength="1000"
			          placeholder="메이트를 찾는 다른 학우들에게 내 생활 습관과 수면 패턴, 원하는 상대의 취향에 대해 자세히 작성해 주세요.
			
예: 잠귀가 어두우신 분이 편해요, 주말에는 거의 고향 내려갑니다!"
			          required>${post.content}</textarea>
            </div>
            
            <div class="writeModalActions">
			    <button type="button" id="deletePostButton"
			            class="btn writeDeleteButton" hidden>
			        삭제하기
			    </button>
			
			    <div class="writeModalRightActions">
			        <button type="button" id="cancelWriteModal"
			                class="btn writeCancelButton">
			            취소
			        </button>
			
			        <button type="submit" id="writeSubmitButton"
			                class="btn writeSubmitButton">
			            모집글 등록하기
			        </button>
			    </div>
			</div>

        </form>
    </section>
</div>
