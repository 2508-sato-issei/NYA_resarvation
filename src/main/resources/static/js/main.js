/**
 * 時間選択制限用
 */
document.addEventListener("DOMContentLoaded", function () {
    const timeSelect = document.getElementById("timeSelect");

    if (timeSelect) {
        const startTime = timeSelect.dataset.start;
        const endTime = timeSelect.dataset.end;

        if (startTime && endTime) {
            generateTimeOptions(timeSelect, startTime, endTime, 30);
        }
    }
});

function generateTimeOptions(selectElement, start, end, interval) {
    const [startHour, startMin] = start.split(":").map(Number);
    const [endHour, endMin] = end.split(":").map(Number);

    let startTotalMinutes = startHour * 60 + startMin;
    let endTotalMinutes = endHour * 60 + endMin;

    if (endTotalMinutes <= startTotalMinutes) {
        endTotalMinutes += 24 * 60;
    }

    const limitMinutes = endTotalMinutes - 60;

    for (let minutes = startTotalMinutes; minutes <= limitMinutes; minutes += interval) {
        const displayMinutes = minutes % (24 * 60);

        const hour = Math.floor(displayMinutes / 60).toString().padStart(2, '0');
        const min = (displayMinutes % 60).toString().padStart(2, '0');
        const timeStr = `${hour}:${min}`;

        const option = document.createElement("option");
        option.value = timeStr;
        option.textContent = timeStr;

        selectElement.appendChild(option);
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
