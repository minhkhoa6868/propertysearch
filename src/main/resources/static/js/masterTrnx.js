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
  document
    .querySelectorAll('input[name="selectedProperties"]')
    .forEach((checkbox) => {
      checkbox.addEventListener("change", function () {
        const row = this.closest("tr");
        if (this.checked) {
          row.classList.add("active");
        } else {
          row.classList.remove("active");
        }
        updateSelectedCount();
      });
    });

  // select all checkbox
  const selectAll = document.getElementById("selectAll");
  if (selectAll) {
    selectAll.addEventListener("change", function () {
      document
        .querySelectorAll('input[name="selectedProperties"]')
        .forEach((checkbox) => {
          checkbox.checked = this.checked;
          const row = checkbox.closest("tr");
          if (this.checked) {
            row.classList.add("active");
          } else {
            row.classList.remove("active");
          }
          updateSelectedCount();
        });
    });
  }
});

// Validation and filter logic
function validateFilters() {
  const nPostal = document.getElementById("nPostal").value;
  const flrAreaMin = document.getElementById("flrAreaMin").value;
  const flrAreaMax = document.getElementById("flrAreaMax").value;
  const contDateFrom = document.getElementById("contDateFrom").value;
  const contDateTo = document.getElementById("contDateTo").value;

  const nPostalError = document.getElementById("nPostalError");
  const flrAreaError = document.getElementById("flrAreaError");
  const contDateError = document.getElementById("contDateError");

  let invalid = false;

  if (nPostal) {
    if (!isNumeric(nPostal) || nPostal.length !== 6) {
      nPostalError.style.display = "block";
      nPostalError.textContent =
        nPostal.length !== 6
          ? "Postal code must be 6 digits"
          : "Please enter a valid number";
      invalid = true;
    } else {
      nPostalError.style.display = "none";
      nPostalError.textContent = "";
    }
  }

  if (flrAreaMin) {
    if (!isNumeric(flrAreaMin) || flrAreaMin < 0) {
      flrAreaError.style.display = "block";
      flrAreaError.textContent = !isNumeric(flrAreaMin)
        ? "Please enter a valid number for Floor Area Min."
        : "Floor Area Min cannot be negative.";
      invalid = true;
    } else if (flrAreaMax && !isMinSmallerThanMax(flrAreaMin, flrAreaMax)) {
      flrAreaError.style.display = "block";
      flrAreaError.textContent =
        "Floor Area Max must be greater than or equal to Min.";
      invalid = true;
    } else {
      flrAreaError.style.display = "none";
      flrAreaError.textContent = "";
    }
  }

  if (flrAreaMax) {
    if (!isNumeric(flrAreaMax) || flrAreaMax < 0) {
      flrAreaError.style.display = "block";
      flrAreaError.textContent = !isNumeric(flrAreaMax)
        ? "Please enter a valid number for Floor Area Max."
        : "Floor Area Max cannot be negative.";
      invalid = true;
    } else if (flrAreaMin && !isMinSmallerThanMax(flrAreaMin, flrAreaMax)) {
      flrAreaError.style.display = "block";
      flrAreaError.textContent =
        "Floor Area Max must be greater than or equal to Min.";
      invalid = true;
    } else {
      flrAreaError.style.display = "none";
      flrAreaError.textContent = "";
    }
  }

  if (
    contDateFrom &&
    contDateTo &&
    !isValidDateRange(contDateFrom, contDateTo)
  ) {
    contDateError.style.display = "block";
    contDateError.textContent = "Invalid date range.";
    invalid = true;
  } else {
    contDateError.style.display = "none";
    contDateError.textContent = "";
  }

  return !invalid;
}

// add event listener for real time validation on filter inputs
document.addEventListener("DOMContentLoaded", function () {
  const textInputs = ["nPostal", "flrAreaMin", "flrAreaMax"];
  const dateInputs = ["contDateFrom", "contDateTo"];

  textInputs.forEach((id) => {
    const input = document.getElementById(id);
    if (input) {
      input.addEventListener("input", function () {
        if (this.value.length > 0) {
          validateFilters();
        } else {
          const errorEl =
            document.getElementById("nPostalError") ||
            document.getElementById("flrAreaError");
          if (errorEl) {
            errorEl.style.display = "none";
            errorEl.textContent = "";
          }
        }
      });
    }
  });

  dateInputs.forEach((id) => {
    const input = document.getElementById(id);
    if (input) {
      input.addEventListener("change", validateFilters);
    }
  });
});

function applyFilters() {
  if (!validateFilters()) {
    alert("Please fix validation errors before applying filters.");
    return;
  }

  const currentParams = new URLSearchParams(window.location.search);
  const params = new URLSearchParams();

  const stNames = document.getElementById("stNames");
  Array.from(stNames.selectedOptions).forEach((opt) => {
    params.append("stNames", opt.value);
  });

  const nPostal = document.getElementById("nPostal").value;
  const flrAreaMin = document.getElementById("flrAreaMin").value;
  const flrAreaMax = document.getElementById("flrAreaMax").value;
  const contDateFrom = document.getElementById("contDateFrom").value;
  const contDateTo = document.getElementById("contDateTo").value;
  const sortField = document.getElementById("sortFieldInput").value;
  const sortDirection = document.getElementById("sortDirectionInput").value;
  const limit = document.getElementById("pageSizeSelect")?.value || "50";

  if (nPostal) params.set("nPostal", nPostal);
  if (flrAreaMin) params.set("flrAreaMin", flrAreaMin);
  if (flrAreaMax) params.set("flrAreaMax", flrAreaMax);
  if (contDateFrom) params.set("contDateFrom", contDateFrom);
  if (contDateTo) params.set("contDateTo", contDateTo);
  if (sortField) params.set("sortField", sortField);
  if (sortDirection) params.set("sortDirection", sortDirection);

  if (currentParams.has("limit"))
    params.set("limit", currentParams.get("limit"));
  if (currentParams.has("offset")) params.set("offset", "0"); // reset to first page

  window.location.href = "/?" + params.toString();
}

function clearFilters() {
  const currentParams = new URLSearchParams(window.location.search);
  const params = new URLSearchParams();

  if (currentParams.has("sortField"))
    params.set("sortField", currentParams.get("sortField"));
  if (currentParams.has("sortDirection"))
    params.set("sortDirection", currentParams.get("sortDirection"));

  if (currentParams.has("limit"))
    params.set("limit", currentParams.get("limit"));
  if (currentParams.has("offset")) params.set("offset", "0"); // reset to first page

  const query = params.toString();
  window.location.href = query ? "/?" + query : "/";
}

function handleSort(btn) {
  const field = btn.getAttribute("data-field");
  const currentField = document.getElementById("sortFieldInput").value;
  const currentDir = document.getElementById("sortDirectionInput").value;

  const newDir =
    currentField === field ? (currentDir === "desc" ? "asc" : "desc") : "asc";

  // copy all current params then just update sort
  const params = new URLSearchParams(window.location.search);
  params.set("sortField", field);
  params.set("sortDirection", newDir);
  params.set("offset", "0");

  window.location.href = "/?" + params.toString();
}

function handlePageSizeChange(select) {
  // copy all current params then just update limit
  const params = new URLSearchParams(window.location.search);
  params.set("limit", select.value);
  params.set("offset", "0");

  window.location.href = "/?" + params.toString();
}

function goToPage(pageOrElement) {
  const params = new URLSearchParams(window.location.search);
  const limit = parseInt(params.get("limit") || "50");

  let page;
  if (typeof pageOrElement === "number") {
    page = pageOrElement;
  } else {
    page = parseInt(pageOrElement.getAttribute("data-page"));
  }

  params.set("limit", limit);
  params.set("offset", (page - 1) * limit);
  window.location.href = "/?" + params.toString();
}

function updateSelectedCount() {
    const selectedCount = document.querySelectorAll('.row-checkbox:checked').length;
    const el = document.querySelector(".selected-count");
    if (el) el.textContent = selectedCount;
}
