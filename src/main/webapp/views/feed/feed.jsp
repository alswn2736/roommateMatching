<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>룸메이트 모집글</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&family=Newsreader:wght@400;700&family=Noto+Sans+KR:wght@400;500;700&display=swap" rel="stylesheet">
	
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
	
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feed/feed.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feed/write.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feed/detail.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feed/apply.css">
</head>

<body>
    <jsp:include page="/views/common/header.jsp" />

    <main class="feedPage">
		<aside class="filterPanel">
		    <div class="filterSection">
		        <label for="keywordSearch">키워드 검색</label>
		
		        <div class="searchInputWrap">
		            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
		                <path d="M14.0001 14.0001L11.1068 11.1068M12.6667 7.33333C12.6667 10.2789 10.2789 12.6667 7.33333 12.6667C4.38781 12.6667 2 10.2789 2 7.33333C2 4.38781 4.38781 2 7.33333 2C10.2789 2 12.6667 4.38781 12.6667 7.33333Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
		            </svg>
		
		            <input type="text" id="keywordSearch"
			           class="form-control"
			           placeholder="키워드를 입력하세요">
		        </div>
		    </div>
		
		    <hr class="filterDivider">
		
		    <div class="filterSection">
		        <p class="filterHeading">흡연 여부</p>
		
		        <label class="filterOption">
			        <input type="checkbox" class="form-check-input"
	           			name="smoking" value="비흡연" checked>
		            <span>비흡연자 선호</span>
		        </label>
		
		        <label class="filterOption">
		            <input type="checkbox" class="form-check-input"
	           			name="smoking" value="흡연가능">
		            <span>흡연 가능자 선호</span>
		        </label>
		    </div>
		
		    <hr class="filterDivider">
		
		    <div class="filterSection">
		        <p class="filterHeading">생활 패턴</p>
		
		        <label class="filterOption">
					<input type="checkbox" class="form-check-input"
	           			name="habit" value="아침형" checked>
		            <span>아침형 인간 (얼리버드)</span>
		        </label>
		
		        <label class="filterOption">
					<input type="checkbox" class="form-check-input"
	           			name="habit" value="저녁형">
		            <span>저녁형 인간 (올빼미)</span>
		        </label>
		
		        <label class="filterOption">
					<input type="checkbox" class="form-check-input"
	           			name="habit" value="상관없음">
		            <span>상관 없음</span>
		        </label>
		    </div>
		
		    <hr class="filterDivider">
		
		    <div class="filterSection">
		        <p class="filterHeading">성격 / 성향</p>
		
		        <label class="filterOption">
					<input type="checkbox" class="form-check-input"
	           			name="personality" value="조용한편" checked>
		            <span>조용한 편</span>
		        </label>
		
		        <label class="filterOption">
		            <input type="checkbox" class="form-check-input"
	           			name="personality" value="활발하고 외향적">
		            <span>활발하고 외향적</span>
		        </label>
		
		        <label class="filterOption">
		        	<input type="checkbox" class="form-check-input"
	           			name="personality" value="개인 생활 존중">
		            <span>개인 생활 존중</span>
		        </label>
		
		        <label class="filterOption">
					<input type="checkbox" class="form-check-input"
	           			name="personality" value="사교적인 메이트">
		            <span>사교적인 메이트</span>
		        </label>
		    </div>
		
		    <button type="button" id="applyFilters" class="filterButton">
		        필터 적용하기
		    </button>
		
		</aside>
		
		
        <section class="feedContent">
            <div class="feedToolbar">
                <h2>
                    전체 모집글
                    <span id="postCount">48</span>개
                </h2>

                <div class="toolbarActions">

                    <select id="sortPosts" class="form-select" aria-label="모집글 정렬">
					    <option value="latest">최신순</option>
					    <option value="oldest">오래된순</option>
					</select>

                    <button type="button" id="openWriteModal"
					        class="btn writeButton">
					    방 찾기 글쓰기
					</button>

                </div>
            </div>
            
            
            <div class="postList" id="postList">

                <article class="postCard"
                         data-id="1"
                         data-title="아침형 룸메 구해요, 비흡연만요"
                         data-room="기숙사 2인실"
                         data-smoking="비흡연"
                         data-habits="아침형 청결 조용한편"
                         data-date="3"
                         data-author="얼리버드"
                         data-author-info="컴퓨터공학과 · 20학번"
                         data-avatar="얼">

                    <div class="avatar">
					    <img src="${pageContext.request.contextPath}/assets/image/feed/kim.jpg" alt="얼리버드 프로필">
					</div>

                    <div class="postInfo">

                        <div class="postTop">
                            <span class="roomBadge">2인실 희망</span>
                            <span class="postDate">비흡연</span>
                        </div>

                        <h3>
                            새 학기 기숙사 2인실 같이 쓸 동갑내기 룸메이트 구합니다!
                        </h3>

                        <div class="postTags">
                            <span>#컴공</span>
                            <span>#저녁형</span>
                            <span>#사교적</span>
                            <span>#에어컨 프리</span>
                        </div>

                        <div class="postBottom">
                            <span>모집 인원: 1명 | 등록일: 2시간 전</span>
                            <span>김민재 (20학번)</span>
                        </div>

                    </div>
                </article>

                <article class="postCard"
                         data-id="2"
                         data-title="생활 패턴 잘 맞는 조용한 경영학과 룸메 한 명 급히 찾아요"
                         data-room="기숙사 3인실"
                         data-smoking="비흡연"
                         data-habits="아침형 청결 조용한편"
                         data-date="2"
                         data-author="이주형"
                         data-author-info="경영학과 · 19학번"
                         data-avatar="이">

                    <div class="avatar">
					    <img src="${pageContext.request.contextPath}/assets/image/feed/lee.jpg" alt="얼리버드 프로필">
					</div>

                    <div class="postInfo">

                        <div class="postTop">
                            <span class="roomBadge">3인실 희망</span>
                            <span class="postDate">비흡연</span>
                            <span class="postDate">얼리버드</span>
                        </div>

                        <h3>
                            생활 패턴 잘 맞는 조용한 경영학과 룸메 한 명 급히 찾아요
                        </h3>

                        <div class="postTags">
                            <span>#경영</span>
                            <span>#아침형</span>
                            <span>#조용한 편</span>
                            <span>#깔끔한 정리</span>
                        </div>

                        <div class="postBottom">
                            <span>모집 인원: 1명 | 등록일: 1일 전</span>
                            <span>이주형 (19학번)</span>
                        </div>

                    </div>
                </article>

                <article class="postCard"
                         data-id="3"
                         data-title="디자인과 야작 많은 편이라 서로 터치 안 하는 룸메 구해요"
                         data-room="기숙사 2인실"
                         data-smoking="비흡연"
                         data-habits="저녁형 청결 개인생활존중"
                         data-date="1"
                         data-author="박서연"
                         data-author-info="산업디자인과 · 21학번"
                         data-avatar="박">

                    <div class="avatar">
					    <img src="${pageContext.request.contextPath}/assets/image/feed/park.jpg" alt="얼리버드 프로필">
					</div>

                    <div class="postInfo">

                        <div class="postTop">
                            <span class="roomBadge">2인실 희망</span>
                            <span class="postDate">흡연 안됨</span>
                            <span class="postDate">개인주의</span>
                        </div>

                        <h3>
                            디자인과 야작 많은 편이라 서로 터치 안 하는 룸메 구해요
                        </h3>

                        <div class="postTags">
                            <span>#산업디자인</span>
                            <span>#새벽형</span>
                            <span>#개인생활</span>
                            <span>#터치없음</span>
                        </div>

                        <div class="postBottom">
                            <span>모집 인원: 1명 | 등록일: 3일 전</span>
                            <span>박서연 (21학번)</span>
                        </div>

                    </div>
                </article>

            </div>
            
            <p id="emptyMessage" class="emptyMessage" hidden>
                조건에 맞는 모집글이 없습니다.
            </p>

        </section>
    </main>
    
    <jsp:include page="/views/feed/write.jsp" />
    <jsp:include page="/views/feed/detail.jsp" />
    <jsp:include page="/views/feed/apply.jsp" />
    
    <script src="${pageContext.request.contextPath}/assets/js/feed/feed.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/feed/write.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/feed/detail.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/feed/apply.js"></script>

</body>
</html>
