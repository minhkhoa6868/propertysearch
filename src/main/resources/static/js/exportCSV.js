function exportToCSV() {
  let csv_data = [];

  const selectedRows = document.querySelectorAll(".row-checkbox:checked");

  // if no row selected, return
  if (selectedRows.length === 0) {
    alert("Please select at least one row to export.");
    return;
  }

  // Get table headers
  const headers = document.querySelectorAll("table thead tr th");
  const headerRow = [];

  headers.forEach((header, index) => {
    // if it is checkbox header, skip
    if (index === 0) return;

    // clone header and rempve sort button if any
    const clone = header.cloneNode(true);
    const sortBtn = clone.querySelector(".sort-btn");
    if (sortBtn) sortBtn.remove();

    let headerText = clone.textContent
      .trim()
      .replace(/\s+/g, " ") // collapse multiple spaces/newlines
      .trim();

    headerText = headerText.replace(/"/g, '""');
    if (headerText.includes(",") || headerText.includes("\n")) {
      headerText = `"${headerText}"`;
    }
    headerRow.push(headerText);
  });

  csv_data.push(headerRow.join(","));

  selectedRows.forEach((checkbox) => {
    const row = checkbox.closest("tr");
    const rowData = [];
    row.querySelectorAll("td").forEach((cell, index) => {
      // if it is checkbox cell, skip
      if (index === 0) return;

      // Escape double quotes in cell data
      let cellText = cell.textContent.trim().replace(/"/g, '""');

      // if class is date, convert to YYYY-MM-DD format
      if (cell.classList.contains("date")) {
        // parse dd/MM/yyyy → YYYY-MM-DD
        const parts = cellText.trim().split("/");
        if (parts.length === 3) {
          cellText = `${parts[2]}-${parts[1]}-${parts[0]}`; // YYYY-MM-DD
        }
      }
      // if class is money, plain number with 2 decimal places
      else if (cell.classList.contains("money")) {
        const num = parseFloat(cellText.replace(/,/g, ""));
        cellText = isNaN(num) ? "" : num.toFixed(2);
      }

      // Wrap cell data in double quotes if it contains a comma or a newline
      if (cellText.includes(",") || cellText.includes("\n")) {
        cellText = `"${cellText}"`;
      }
      rowData.push(cellText);
    });
    csv_data.push(rowData.join(","));
  });

  const csvString = csv_data.join("\n");
  const blob = new Blob(["\uFEFF" + csvString], {
    type: "text/csv;charset=utf-8",
  });
  const url = URL.createObjectURL(blob);

  const a = document.createElement("a");
  a.href = url;

  const now = new Date();
  const pad = (n) => String(n).padStart(2, "0");

  const yyyy = now.getFullYear();
  // Months are zero-indexed, so we add 1
  const mm = pad(now.getMonth() + 1);
  const dd = pad(now.getDate());
  const hh = pad(now.getHours());
  const min = pad(now.getMinutes());
  const ss = pad(now.getSeconds());

  a.download = `PropertySearch_Export_${yyyy}${mm}${dd}_${hh}${min}${ss}.csv`;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}
