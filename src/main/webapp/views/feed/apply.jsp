<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<div class="modalOverlay" id="applyModalOverlay" hidden>
    <section class="applyModal" role="dialog" aria-modal="true" aria-labelledby="applyModalTitle">
    
        <div class="applyHeading">
            <h2 id="applyModalTitle">룸메이트 신청</h2>

            <p class="applyNotice">
                신청 시 나의 기본 정보(공개 설정된 항목)가 상대방에게 전송됩니다.
            </p>
        </div>
        
        <div class="publicInfoBox">
            <h3>내 공개 정보 미리보기</h3>
            
            <div class="publicInfoRows">
                <div class="publicInfoRow">
                    <span>이름</span>
                    <strong>김*재</strong>
                </div>

                <div class="publicInfoRow">
                    <span>학번</span>
                    <strong>20학번</strong>
                </div>

                <div class="publicInfoRow">
                    <span>흡연</span>
                    <strong>비흡연</strong>
                </div>
            </div>
        </div>
        
        <div class="applyMessageField">
            <label for="applyMessage">보낼 한 마디</label>
            <div class="applyMessageBox">
                <textarea
                	id="applyMessage"
                    maxlength="300"
                    placeholder="안녕하세요! 룸메 구하는 글 보고 신청합니다. 저도 비흡연에 잠귀가 어두운 편이라..."
                    required></textarea>
                    
                <div class="characterCount">
                    <span id="applyCharacterCount">0</span> / 300자
                </div>
            </div>
        </div>
        
        <div class="applyActions">
            <button type="button" id="cancelApplyModal" class="applyCancelButton">
            	취소
            </button>
            <button type="submit" id="submitApply" class="applySubmitButton">
			    신청하기
			</button>
        </div>
    </section>
</div>
