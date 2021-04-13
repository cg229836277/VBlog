export const formatCurDate = () => {
  let date = new Date()
  let year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()
  let hours = date.getHours()
  let minutes = date.getMinutes()
  if (month < 10) {
    month = '0' + month
  }
  if (day < 10) {
    day = '0' + day
  }
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes
}
