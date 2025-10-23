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

        // 予約済み時間を選択状態にする
        const currentTimeElement = document.getElementById("currentTime");
        if (currentTimeElement && currentTimeElement.value) {
            timeSelect.value = currentTimeElement.value;
        }
    }
});

function generateTimeOptions(selectElement, start, end, interval) {
    const [startHour, startMin] = start.split(":").map(Number);
    const [endHour, endMin] = end.split(":").map(Number);

    const startTotalMinutes = startHour * 60 + startMin;
    const endTotalMinutes = endHour * 60 + endMin;

    for (let minutes = startTotalMinutes; minutes <= endTotalMinutes; minutes += interval) {
        const hour = Math.floor(minutes / 60).toString().padStart(2, '0');
        const min = (minutes % 60).toString().padStart(2, '0');
        const timeStr = `${hour}:${min}`;

        const option = document.createElement("option");
        option.value = timeStr;
        option.textContent = timeStr;

        selectElement.appendChild(option);
    }
}
