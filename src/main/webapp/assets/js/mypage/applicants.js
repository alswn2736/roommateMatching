(function () {
    var ctx = document.body.dataset.ctx || '';
    var POLL_MS = 3000;

    // ===== 공통: 서버 요청 =====
    function request(method, url, params) {
        var opts = { method: method, headers: { 'X-Requested-With': 'XMLHttpRequest' } };
        if (method === 'GET' && params) {
            url += '?' + new URLSearchParams(params).toString();
        } else if (params) {
            opts.body = new URLSearchParams(params);
        }
        return fetch(ctx + url, opts).then(function (res) {
            return res.json().then(function (data) {
                if (res.status === 401 && data.redirect) {
                    location.href = data.redirect;
                    throw new Error(data.message);
                }
                if (!data.ok) throw new Error(data.message || '요청에 실패했습니다.');
                return data;
            });
        });
    }

    function formatTime(ms) {
        if (!ms) return '';
        var d = new Date(ms);
        var h = d.getHours();
        var m = d.getMinutes();
        var ampm = h < 12 ? '오전' : '오후';
        h = h % 12 === 0 ? 12 : h % 12;
        return ampm + ' ' + h + ':' + (m < 10 ? '0' + m : m);
    }

    // ===== 채팅 =====
    function Chat(card) {
        this.card = card;
        this.applyId = card.dataset.applyId;
        this.history = card.querySelector('.chatHistory');
        this.input = card.querySelector('.inputWrapper');
        this.sendBtn = card.querySelector('.btnSend');
        this.lastId = 0;
        this.timer = null;
        this.loading = false;

        var self = this;
        this.sendBtn.addEventListener('click', function () { self.send(); });
        this.input.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' && !e.isComposing) {
                e.preventDefault();
                self.send();
            }
        });
    }

    Chat.prototype.append = function (msg) {
        if (msg.msgId <= this.lastId) return; // 중복 방지
        this.lastId = msg.msgId;

        var empty = this.history.querySelector('.chatEmpty');
        if (empty) empty.remove();

        var row = document.createElement('div');
        var bubble = document.createElement('div');
        var text = document.createElement('span');
        var time = document.createElement('span');

        row.className = msg.mine ? 'sent' : 'received';
        bubble.className = msg.mine ? 'sentBubble' : 'receivedBubble';
        time.className = 'chatTime';
        text.textContent = msg.content;          // textContent 로 넣어서 XSS 방지
        time.textContent = formatTime(msg.sentAt);
        bubble.appendChild(text);

        if (msg.mine) {
            row.appendChild(time);
            row.appendChild(bubble);
        } else {
            row.appendChild(bubble);
            row.appendChild(time);
        }
        this.history.appendChild(row);
        this.history.scrollTop = this.history.scrollHeight;
    };

    Chat.prototype.load = function () {
        var self = this;
        if (self.loading) return;
        self.loading = true;
        request('GET', '/mypage/chat/list', { applyId: self.applyId, after: self.lastId })
            .then(function (data) {
                data.messages.forEach(function (m) { self.append(m); });
                if (self.lastId === 0 && !self.history.querySelector('.chatEmpty')) {
                    var empty = document.createElement('span');
                    empty.className = 'chatEmpty';
                    empty.textContent = '아직 주고받은 메시지가 없습니다.';
                    self.history.appendChild(empty);
                }
            })
            .catch(function (err) {
                self.stop();
                console.error(err);
            })
            .finally(function () { self.loading = false; });
    };

    Chat.prototype.start = function () {
        var self = this;
        self.load();
        self.timer = setInterval(function () { self.load(); }, POLL_MS);
        self.input.focus();
    };

    Chat.prototype.stop = function () {
        clearInterval(this.timer);
        this.timer = null;
    };

    Chat.prototype.send = function () {
        var self = this;
        var content = self.input.value.trim();
        if (!content) return;
        self.sendBtn.disabled = true;
        request('POST', '/mypage/chat/send', { applyId: self.applyId, content: content })
            .then(function (data) {
                self.input.value = '';
                self.append(data.chat);
            })
            .catch(function (err) { alert(err.message); })
            .finally(function () {
                self.sendBtn.disabled = false;
                self.input.focus();
            });
    };

    // ===== 카드별 이벤트 =====
    document.querySelectorAll('[data-apply-id]').forEach(function (card) {
        var chat = new Chat(card);
        var chatBtn = card.querySelector('.btnChat');
        var chatText = card.querySelector('.btnChatText');

        chatBtn.addEventListener('click', function () {
            var open = card.classList.contains('applicantCardSelected');
            card.classList.toggle('applicantCardSelected', !open);
            card.classList.toggle('applicantCardNone', open);
            chatText.textContent = open ? '채팅' : '채팅중';
            if (open) {
                chat.stop();
            } else {
                chat.start();
            }
        });

        var acceptBtn = card.querySelector('.btnAccept');
        var declineBtn = card.querySelector('.btnDecline');

        function decide(accept) {
            var name = card.querySelector('.name').textContent.trim();
            var msg = accept
                ? name + ' 님의 신청을 수락할까요?'
                : name + ' 님의 신청을 거절할까요? 거절하면 되돌릴 수 없습니다.';
            if (!confirm(msg)) return;

            acceptBtn.disabled = true;
            declineBtn.disabled = true;
            request('POST', accept ? '/mypage/applicants/accept' : '/mypage/applicants/reject',
                    { applyId: card.dataset.applyId })
                .then(function (data) {
                    alert(data.message);
                    location.reload(); // 대기 인원/모집 상태가 바뀌므로 새로 그림
                })
                .catch(function (err) {
                    alert(err.message);
                    acceptBtn.disabled = false;
                    declineBtn.disabled = false;
                });
        }

        if (acceptBtn) acceptBtn.addEventListener('click', function () { decide(true); });
        if (declineBtn) declineBtn.addEventListener('click', function () { decide(false); });
    });
})();
