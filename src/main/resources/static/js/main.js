/**
 * 時間選択制限用
 */
document.addEventListener("DOMContentLoaded", function () {
    const timeSelect = document.getElementById("timeSelect");
    const dateInput = document.querySelector("input[type='date']");

    if (timeSelect) {
        const startTime = timeSelect.dataset.start;
        const endTime = timeSelect.dataset.end;

        if (startTime && endTime) {
            generateTimeOptions(timeSelect, startTime, endTime, 30);
        }

        if (dateInput) {
            dateInput.addEventListener("change", function () {
                updateTimeOptions(dateInput, timeSelect, startTime, endTime);
            });
        }

        // 予約済み時間を選択状態にする
        const currentTimeElement = document.getElementById("currentTime");
        if (currentTimeElement && currentTimeElement.value) {
            timeSelect.value = currentTimeElement.value;
        }
    }
});

/**
 * 営業時間内の時間帯を生成
 */
function generateTimeOptions(selectElement, start, end, interval, currentLimit = null) {
    selectElement.innerHTML = "";

    const [startHour, startMin] = start.split(":").map(Number);
    const [endHour, endMin] = end.split(":").map(Number);

    let startTotalMinutes = startHour * 60 + startMin;
    let endTotalMinutes = endHour * 60 + endMin;

    if (endTotalMinutes <= startTotalMinutes) {
        endTotalMinutes += 24 * 60;
    }

    const limitMinutes = endTotalMinutes - 60; // L.Oは営業終了1時間前まで

    for (let minutes = startTotalMinutes; minutes <= limitMinutes; minutes += interval) {
        const displayMinutes = minutes % (24 * 60);

        const hour = Math.floor(displayMinutes / 60).toString().padStart(2, '0');
        const min = (displayMinutes % 60).toString().padStart(2, '0');
        const timeStr = `${hour}:${min}`;

        // 現在時刻より前の時間を除外（currentLimit が設定されているときだけ）
        if (currentLimit !== null && displayMinutes < currentLimit) continue;

        const option = document.createElement("option");
        option.value = timeStr;
        option.textContent = timeStr;

        selectElement.appendChild(option);
    }
}

/**
 * 日付変更時に、今日の場合のみ「今より前の時間」を除外して再生成
 */
function updateTimeOptions(dateInput, selectElement, start, end) {
    const selectedDate = new Date(dateInput.value);
    const today = new Date();

    // 今日の日付と一致するか確認
    const isToday =
        selectedDate.getFullYear() === today.getFullYear() &&
        selectedDate.getMonth() === today.getMonth() &&
        selectedDate.getDate() === today.getDate();

    if (isToday) {
        const currentMinutes = today.getHours() * 60 + today.getMinutes();
        generateTimeOptions(selectElement, start, end, 30, currentMinutes);
    } else {
        generateTimeOptions(selectElement, start, end, 30);
    }
}

//店舗削除確認
function CheckDelete(){
	if(confirm('店舗を削除しますか？')){
		return true;
	} else{
		return false;
	}
}


//ユーザー有効確認
function CheckValid(){
	if(confirm('ユーザーを有効にしますか？')){
		return true;
	} else{
		return false;
	}
}

//ユーザー有効確認
function CheckStop(){
	if(confirm('ユーザーを停止しますか？')){
		return true;
	} else{
		return false;
	}
}


/**
 * 予約確認モーダル
 */
document.addEventListener('DOMContentLoaded', function () {
    // 新規予約ページ用
    const reservationForm = document.getElementById('reservationForm');
    if (reservationForm) {
        const confirmButton = document.getElementById('confirmButton');
        const modalSubmitBtn = document.getElementById('modalSubmitBtn');
        const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));

        confirmButton.addEventListener('click', function (event) {
            event.preventDefault();
            const date = document.getElementById('date').value;
            const time = document.getElementById('timeSelect').value;
            const headcount = document.getElementById('headcount').value;
            document.querySelector('#confirmModal .modal-body').innerHTML =
                `以下の内容で予約しますか？<br>
                 日時：${date} ${time}<br>
                 人数：${headcount}名`;
            confirmModal.show();
        });

        modalSubmitBtn.addEventListener('click', function () {
            reservationForm.submit();
        });
    }

    // 予約変更ページ用
    const editForm = document.getElementById('reservationEditForm');
    if (editForm) {
        const confirmButton = document.getElementById('confirmButton');
        const modalSubmitBtn = document.getElementById('modalSubmitBtn');
        const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));

        confirmButton.addEventListener('click', function (event) {
            event.preventDefault();
            const date = document.getElementById('reservationDate').value;
            const time = document.getElementById('timeSelect').value;
            const headcount = document.getElementById('headcount').value;
            document.querySelector('#confirmModal .modal-body').innerHTML =
                `以下の内容に予約を変更しますか？<br>
                 日時：${date} ${time}<br>
                 人数：${headcount}名`;
            confirmModal.show();
        });

        modalSubmitBtn.addEventListener('click', function () {
            editForm.submit();
        });
    }
});