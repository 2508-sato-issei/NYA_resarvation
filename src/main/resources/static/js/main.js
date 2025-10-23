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
