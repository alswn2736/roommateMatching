<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<div class="modalOverlay" id="detailModalOverlay" hidden>
    <section class="detailModal" role="dialog" aria-modal="true" aria-labelledby="detailTitle">
        <div class="detailHeading">

            <div class="detailTop">
                <span class="roomBadge" id="detailRoomType">기숙사 2인실</span>

                <button type="button" id="closeDetailModal" class="detailCloseButton" aria-label="닫기">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16.668" height="16.668" viewBox="0 0 19 19" fill="none">
	                    <path d="M11.8342 6.8338L9.334 9.334M9.334 9.334L6.8338 11.8342M9.334 9.334L6.8338 6.8338M9.334 9.334L11.8342 11.8342M17.668 9.334C17.668 13.9367 13.9367 17.668 9.334 17.668C4.73126 17.668 1 13.9367 1 9.334C1 4.73126 4.73126 1 9.334 1C13.9367 1 17.668 4.73126 17.668 9.334Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
	                </svg>
                </button>
            </div>

            <h2 id="detailTitle">아침형 룸메 구해요, 비흡연만요</h2>

        </div>
        
        <div class="detailAuthor">
            <div class="detailAvatar" id="detailAvatar">
                <img
                    id="detailAvatarImage"
                    src="${pageContext.request.contextPath}/assets/image/feed/profile.jpg"
                    alt="프로필 사진">
                <span id="detailAvatarFallback" hidden>얼</span>
            </div>

            <div>
                <strong id="detailAuthorName">얼리버드</strong>
                <p id="detailAuthorInfo">컴퓨터공학과 · 20학번</p>
            </div>
        </div>
        
        <hr>
        
        <div class="detailFacts">

            <div class="detailFact">
                <span>모집 인원</span>
                <strong>1명</strong>
            </div>

            <div class="detailFact">
                <span>희망 기간</span>
                <strong>1학기</strong>
            </div>

            <div class="detailFact">
                <span>흡연 여부</span>
                <strong id="detailSmoking">비흡연</strong>
            </div>

            <div class="detailFact">
                <span>수면 패턴</span>
                <strong id="detailSleep">아침형 (6~7시 기상)</strong>
            </div>

        </div>
        
        <div class="detailTags" id="detailTags">
            <span>#아침형</span>
            <span>#비흡연</span>
            <span>#깔끔</span>
            <span>#조용한 편</span>
        </div>
        
        <p class="detailDescription" id="detailDescription">
            새 학기를 함께할 조용하고 깔끔한 룸메이트를 찾고 있습니다. 아침형 생활 패턴을 가지신 분과 좋은 메이트가 되고 싶어요! 시험 기간에도 서로 조용하며 배려할 줄 아는 분이면 좋겠습니다. 편하게 연락주세요!
        </p>
        
        <button type="button" class="detailApplyButton" id="openApplyModal">
        	신청하기
        </button>
    </section>
</div>