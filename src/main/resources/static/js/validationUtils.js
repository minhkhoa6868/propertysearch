function isNumeric (str) {
  if (typeof str !== "string") return false;
  return !isNaN(str) && !isNaN(parseFloat(str));
};

function isMinSmallerThanMax (min, max) {
  if (min === "" || max === "") return true;
  return parseFloat(min) <= parseFloat(max);
}

function isValidDateRange (from, to) {
  if (from === "" || to === "") return true;
  return new Date(from) <= new Date(to);
}
