// Initialize TomSelect for multi-select dropdown
document.addEventListener("DOMContentLoaded", function () {
  new TomSelect("#stNames", {
    plugins: ["remove_button"],
    create: false,
    maxItems: null,
  });
});

// select checkbox logic
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('input[name="selectedProperties"]').forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            const row = this.closest("tr");
            if (this.checked) {
                row.classList.add("active");
            } else {
                row.classList.remove("active");
            }
        });
    });

    // select all checkbox
    const selectAll = document.getElementById("selectAll");
    if (selectAll) {
        selectAll.addEventListener("change", function () {
            document.querySelectorAll('input[name="selectedProperties"]').forEach(checkbox => {
                checkbox.checked = this.checked;
                const row = checkbox.closest("tr");
                if (this.checked) {
                    row.classList.add("active");
                } else {
                    row.classList.remove("active");
                }
            });
        });
    }
});

function applyFilters() {
    const param = new URLSearchParams();

    const stNames = document.getElementById("stNames");
    Array.from(stNames.selectedOptions).forEach(opt => {
        param.append("stNames", opt.value);
    });

    const nPostal = document.getElementById("nPostal").value;
    const flrAreaMin = document.getElementById("flrAreaMin").value;
    const flrAreaMax = document.getElementById("flrAreaMax").value;
    const contDateFrom = document.getElementById("contDateFrom").value;
    const contDateTo = document.getElementById("contDateTo").value;
    const sortField = document.getElementById("sortFieldInput").value;
    const sortDirection = document.getElementById("sortDirectionInput").value;
    const limit = document.getElementById("pageSizeSelect")?.value || "50";

    if (nPostal) param.set("nPostal", nPostal);
    if (flrAreaMin) param.set("flrAreaMin", flrAreaMin);
    if (flrAreaMax) param.set("flrAreaMax", flrAreaMax);
    if (contDateFrom) param.set("contDateFrom", contDateFrom);
    if (contDateTo) param.set("contDateTo", contDateTo);
    if (sortField) param.set("sortField", sortField);
    if (sortDirection) param.set("sortDirection", sortDirection);

    param.set("limit", limit);
    param.set("offset", 0); // reset to first page

    window.location.href = "/?" + param.toString();
}

function clearFilters() {
  const param = new URLSearchParams();

  const sortField = document.getElementById("sortFieldInput").value;
  const sortDirection = document.getElementById("sortDirectionInput").value;
  const limit = document.getElementById("pageSizeSelect").value;

  if (sortField) param.set("sortField", sortField);
  if (sortDirection) param.set("sortDirection", sortDirection);
  if (limit) param.set("limit", limit);
  param.set("offset", 0); // reset to first page

  window.location.href = "/?" + param.toString();
}

function handleSort(btn) {
  const field = btn.getAttribute("data-field");
  const currentField = document.getElementById("sortFieldInput").value;
  const currentDir = document.getElementById("sortDirectionInput").value;

  // if clicking the same column, toggle direction; otherwise default to desc
  let newDir;
  if (currentField === field) {
    newDir = currentDir === "desc" ? "asc" : "desc";
  } else {
    newDir = "asc";
  }

  // build clean URL instead of submitting form with empty params
    const params = new URLSearchParams();

    // only add filters if they have values
    const stNames = document.getElementById("stNames");
    Array.from(stNames.selectedOptions).forEach(opt => {
        params.append("stNames", opt.value);
    });

    const nPostal = document.getElementById("nPostal").value;
    const flrAreaMin = document.getElementById("flrAreaMin").value;
    const flrAreaMax = document.getElementById("flrAreaMax").value;
    const contDateFrom = document.getElementById("contDateFrom").value;
    const contDateTo = document.getElementById("contDateTo").value;
    const limit = document.getElementById("pageSizeSelect")?.value || "50";

    if (nPostal) params.set("nPostal", nPostal);
    if (flrAreaMin) params.set("flrAreaMin", flrAreaMin);
    if (flrAreaMax) params.set("flrAreaMax", flrAreaMax);
    if (contDateFrom) params.set("contDateFrom", contDateFrom);
    if (contDateTo) params.set("contDateTo", contDateTo);

    params.set("sortField", field);
    params.set("sortDirection", newDir);
    params.set("limit", limit);
    params.set("offset", "0");

    window.location.href = "/?" + params.toString();
}

function handlePageSizeChange(select) {
  const param = new URLSearchParams();

  const stNamesSelect = document.getElementById("stNames");
  Array.from(stNamesSelect.selectedOptions).forEach((opt) => {
    param.append("stNames", opt.value);
  });

  const nPostal = document.getElementById("nPostal").value;
  const flrAreaMin = document.getElementById("flrAreaMin").value;
  const flrAreaMax = document.getElementById("flrAreaMax").value;
  const contDateFrom = document.getElementById("contDateFrom").value;
  const contDateTo = document.getElementById("contDateTo").value;
  const sortField = document.getElementById("sortFieldInput").value;
  const sortDirection = document.getElementById("sortDirectionInput").value;

  if (nPostal) param.set("nPostal", nPostal);
  if (flrAreaMin) param.set("flrAreaMin", flrAreaMin);
  if (flrAreaMax) param.set("flrAreaMax", flrAreaMax);
  if (contDateFrom) param.set("contDateFrom", contDateFrom);
  if (contDateTo) param.set("contDateTo", contDateTo);
  if (sortField) param.set("sortField", sortField);
  if (sortDirection) param.set("sortDirection", sortDirection);

  param.set("limit", select.value);
  param.set("offset", "0");

  window.location.href = "/?" + param.toString();
}

function goToPage(pageOrElement) {
    const params = new URLSearchParams(window.location.search);
    const limit = parseInt(params.get("limit") || "50");

    let page;
    if (typeof pageOrElement === 'number') {
        page = pageOrElement;
    } else {
        page = parseInt(pageOrElement.getAttribute("data-page"));
    }

    params.set("limit", limit);
    params.set("offset", (page - 1) * limit);
    window.location.href = "/?" + params.toString();
}
