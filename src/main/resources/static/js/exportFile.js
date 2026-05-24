function getExportFileName(fileType) {
  const now = new Date();
  const pad = (n) => String(n).padStart(2, "0");

  const yyyy = now.getFullYear();
  // Months are zero-indexed, so we add 1
  const mm = pad(now.getMonth() + 1);
  const dd = pad(now.getDate());
  const hh = pad(now.getHours());
  const min = pad(now.getMinutes());
  const ss = pad(now.getSeconds());

  return `PropertySearch_Export_${yyyy}${mm}${dd}_${hh}${min}${ss}.${fileType}`;
}

function getSelectedData() {
  const headers = document.querySelectorAll("table thead tr th");
  const headerRow = [];

  headers.forEach((header, index) => {
    // if it is checkbox header, skip
    if (index === 0) return;

    // clone header and rempve sort button if any
    const clone = header.cloneNode(true);
    const sortBtn = clone.querySelector(".sort-btn")?.remove();

    let headerText = clone.textContent
      .trim()
      .replace(/\s+/g, " ") // collapse multiple spaces/newlines
      .trim();

    headerRow.push(headerText);
  });

  const selectedRows = document.querySelectorAll(".row-checkbox:checked");
  const rows = [];

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
        // parse dd/MM/yyyy -> YYYY-MM-DD
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

      rowData.push(cellText);
    });
    rows.push(rowData);
  });

  return { header: headerRow, rows: rows };
}

function exportToCSV() {
  let csv_data = [];

  const selectedRows = document.querySelectorAll(".row-checkbox:checked");

  // if no row selected, return
  if (selectedRows.length === 0) {
    alert("Please select at least one row to export.");
    return;
  }

  const { header, rows } = getSelectedData();

  const headerRow = header.map((h) => {
    h = h.replace(/"/g, '""');
    return h.includes(",") || h.includes("\n") ? `"${h}"` : h;
  });
  csv_data.push(headerRow.join(","));

  rows.forEach((rowData) => {
    const escaped = rowData.map((cell) => {
      cell = String(cell).replace(/"/g, '""');
      return cell.includes(",") || cell.includes("\n") ? `"${cell}"` : cell;
    });
    csv_data.push(escaped.join(","));
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

  a.download = getExportFileName("csv");
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}

function exportToPDF() {
  const selectedRows = document.querySelectorAll(".row-checkbox:checked");

  // if no row selected, return
  if (selectedRows.length === 0) {
    alert("Please select at least one row to export.");
    return;
  }

  const { jsPDF } = window.jspdf;
  var doc = new jsPDF({ orientation: "landscape" });

  const { header, rows } = getSelectedData();

  const pageSize = doc.internal.pageSize;
  const pageWidth = pageSize.width ? pageSize.width : pageSize.getWidth();
  const pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();

  // title
  doc.setFontSize(14);
  doc.text(
    "Property Search Results — MasterTrnx",
    pageWidth / 2, // x center
    10, // y position
    { align: "center" },
  );

  // timestamp
  const now = new Date();
  const pad = (n) => String(n).padStart(2, "0");

  const yyyy = now.getFullYear();
  // Months are zero-indexed, so we add 1
  const mm = pad(now.getMonth() + 1);
  const dd = pad(now.getDate());
  const hh = pad(now.getHours());
  const min = pad(now.getMinutes());
  const ss = pad(now.getSeconds());

  doc.setFontSize(10);
  doc.setTextColor(100, 100, 100);
  doc.text(
    `Exported on ${yyyy}-${mm}-${dd} at ${hh}:${min}:${ss}`,
    pageWidth / 2, // x center
    15, // y position
    { align: "center" },
  );

  doc.setTextColor(0, 0, 0); // reset text color for table

  doc.autoTable({
    head: [header],
    body: rows,
    styles: { fontSize: 8 },
    theme: "grid",
    startY: 20,
    tableWidth: "auto",
    headStyles: {
      fillColor: [173, 216, 230], // light blue
      textColor: [20, 18, 16], // dark text
      fontStyle: "bold",
      halign: "center",
    },
    columnStyles: {
      0: { cellWidth: 20 }, // Project Name
      1: { cellWidth: 25 }, // Address
    },
  });

  // page numbers
  const pageCount = doc.internal.getNumberOfPages();
  for (let i = 1; i <= pageCount; i++) {
    doc.setPage(i);
    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100);
    doc.text(`Page ${i} of ${pageCount}`, pageWidth / 2, pageHeight - 10, {
      align: "center",
    });
  }

  doc.save(getExportFileName("pdf"));
}
