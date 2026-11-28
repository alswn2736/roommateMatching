<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
		<form id="feedForm" action="${pageContext.request.contextPath}/app/feed" method="get">
		    <aside class="filterPanel">
		
		        <div class="filterSection">
		            <label for="keywordSearch">키워드 검색</label>
		            <div class="searchInputWrap">
		                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
		                    <path d="M14.0001 14.0001L11.1068 11.1068M12.6667 7.33333C12.6667 10.2789 10.2789 12.6667 7.33333 12.6667C4.38781 12.6667 2 10.2789 2 7.33333C2 4.38781 4.38781 2 7.33333 2C10.2789 2 12.6667 4.38781 12.6667 7.33333Z" stroke-width="2" stroke-linecap="round" stroke="#7C6F72"/>
		                </svg>
		
		                <input type="text" id="keywordSearch"
		                       name="keyword"
		                       class="form-control"
		                       placeholder="키워드를 입력하세요"
		                       value="${param.keyword}">
		            </div>
		        </div>
		
		        <hr class="filterDivider">
		
		        <div class="filterSection">
		            <p class="filterHeading">흡연 여부</p>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="smoking" value="N"
		                       ${fn:contains(fn:join(paramValues.smoking, ','), 'N') ? 'checked' : ''}>
		                <span>비흡연자 선호</span>
		            </label>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="smoking" value="Y"
		                       ${fn:contains(fn:join(paramValues.smoking, ','), 'Y') ? 'checked' : ''}>
		                <span>흡연 가능자 선호</span>
		            </label>
		        </div>
		
		        <hr class="filterDivider">
		
		        <div class="filterSection">
		            <p class="filterHeading">생활 패턴</p>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="sleepTime" value="M"
		                       ${fn:contains(fn:join(paramValues.sleepTime, ','), 'M') ? 'checked' : ''}>
		                <span>아침형 (얼리버드)</span>
		            </label>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="sleepTime" value="E"
		                       ${fn:contains(fn:join(paramValues.sleepTime, ','), 'E') ? 'checked' : ''}>
		                <span>저녁형 (올빼미)</span>
		            </label>
		        </div>
		
		        <hr class="filterDivider">
		
		        <div class="filterSection">
		            <p class="filterHeading">성격 / 성향</p>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="sensitivity" value="Q"
		                       ${fn:contains(fn:join(paramValues.sensitivity, ','), 'Q') ? 'checked' : ''}>
		                <span>조용한 편</span>
		            </label>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="sensitivity" value="S"
		                       ${fn:contains(fn:join(paramValues.sensitivity, ','), 'S') ? 'checked' : ''}>
		                <span>사교적인 편</span>
		            </label>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="airCon" value="Y"
		                       ${param.airCon == 'Y' ? 'checked' : ''}>
		                <span>에어컨 빵빵</span>
		            </label>
		
		            <label class="filterOption">
		                <input type="checkbox" class="form-check-input"
		                       name="clean" value="Y"
		                       ${param.clean == 'Y' ? 'checked' : ''}>
		                <span>위생청결 철저</span>
		            </label>
		        </div>
		
		        <button type="submit" id="applyFilters" class="filterButton">
		            필터 적용하기
		        </button>
		
		    </aside>
		</form>
		
		
        <section class="feedContent">
            <div class="feedToolbar">
                <h2>
                    전체 모집글
                    <span id="postCount">${postCount}</span>개
                </h2>

                <div class="toolbarActions">

                    <select id="sortPosts" name="sort" form="feedForm" class="form-select" aria-label="모집글 정렬" onchange="this.form.submit()">
					    <option value="latest" ${param.sort == 'oldest' ? '' : 'selected'}>최신순</option>
					    <option value="oldest" ${param.sort == 'oldest' ? 'selected' : ''}>오래된순</option>
					</select>

                    <button type="button" id="openWriteModal"  class="btn writeButton">
					    방 찾기 글쓰기
					</button>

                </div>
            </div>
            
            <c:if test="${not empty sessionScope.applyMessage}">
				<script>
			        alert("${sessionScope.applyMessage}");
			    </script>
			    <c:remove var="applyMessage" scope="session" />
			</c:if>
            
			<div class="postList" id="postList">
				<c:forEach var="post" items="${postList}">
					<article class="postCard"
							data-id="${post.postId}"
							data-writer-id="${post.writerId}"
							data-title="${post.title}"
							data-room="${post.roomType == 'DOUBLE' ? '2인실' : '4인실'}"
							data-room-type="${post.roomType}"
							data-kw-smoking="${post.kwSmoking}"
							data-kw-sleeptime="${post.kwSleepTime}"
							data-kw-sensitivity="${post.kwSensitivity}"
							data-kw-aircon="${post.kwAirCon}"
							data-kw-clean="${post.kwClean}"
							data-capacity="${post.capacity}"
			                data-smoking="${post.kwSmoking == 'Y' ? '흡연가능' : (post.kwSmoking == 'N' ? '비흡연' : '')}"
			                data-habits="${post.kwSleepTime == 'M' ? '아침형' : (post.kwSleepTime == 'E' ? '저녁형' : '')} 
			                		${post.kwSensitivity == 'Q' ? '조용한편' : (post.kwSensitivity == 'S' ? '사교적인편' : '')} 
			                		${post.kwAirCon == 'Y' ? '에어컨빵빵' : ''} ${post.kwClean == 'Y' ? '위생청결철저' : ''}"
			                data-date="${post.createAt.time}"
			                data-content="${post.content}"
			                data-nickname="${post.nickname}"
			                data-major="${post.major}"
							data-studentid="${post.studentId}"
							data-profile="${post.profileImg}"
							data-author-info="${fn:substring(post.studentId, 2, 4)}학번">
							
						<button type="button" class="editPostButton">
						    수정
						</button>
						
			            <div class="avatar">
						    <c:choose>
						        <c:when test="${not empty post.profileImg}">
						            <img src="${post.profileImg}" alt="프로필 이미지">
						        </c:when>
						        <c:otherwise>
						            <span>룸</span>
						        </c:otherwise>
						    </c:choose>
						</div>
			
			            <div class="postInfo">
			                <div class="postTop">
								
			                    <span class="roomBadge">기숙사 ${post.roomType == 'DOUBLE' ? '2인실' : '4인실'}</span>
			
			                    <c:if test="${post.kwSmoking == 'Y'}">
			                        <span class="postDate">흡연</span>
			                    </c:if>
			
			                    <c:if test="${post.kwSmoking == 'N'}">
			                        <span class="postDate">비흡연</span>
			                    </c:if>
			                </div>
			
			                <h3>${post.title}</h3>
			
			                <div class="postTags">
			                    <c:if test="${post.kwSleepTime == 'M'}">
			                        <span>#아침형</span>
			                    </c:if>
			
			                    <c:if test="${post.kwSleepTime == 'E'}">
			                        <span>#저녁형</span>
			                    </c:if>
			
			                    <c:if test="${post.kwSensitivity == 'Q'}">
			                        <span>#조용한 편</span>
			                    </c:if>
			
			                    <c:if test="${post.kwSensitivity == 'S'}">
			                        <span>#사교적인 편</span>
			                    </c:if>
			
			                    <c:if test="${post.kwAirCon == 'Y'}">
			                        <span>#에어컨 빵빵</span>
			                    </c:if>
			
			                    <c:if test="${post.kwClean == 'Y'}">
			                        <span>#위생청결 철저</span>
			                    </c:if>
			                </div>
			
			                <!-- detail 사용할 게시글 내용 -->
			                <div class="postDescription" hidden>
			                    <c:out value="${post.content}" />
			                </div>
			
			                <div class="postBottom">
			                    <span>
			                        모집 인원: ${post.capacity}명
			                        | 등록일: <span class="relativeDate"></span>
			                    </span>
			                    
			                    <span class="postAuthor">
							        <strong>
							            <c:out value="${post.nickname}" default="익명" />
							        </strong>
							        (<c:out value="${fn:substring(post.studentId, 2, 4)}" />학번)
							    </span>
			                </div>
			            </div>
			        </article>
			    </c:forEach>
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
