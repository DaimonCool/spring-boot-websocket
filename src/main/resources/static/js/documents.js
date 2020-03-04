let fileUpload = document.querySelector('#fileUpload');

function download(url) {
    document.getElementById('my_iframe').src = url;
};
//download("https://daimon-bucket.s3.us-east-2.amazonaws.com/linking_learning_path_courses_script.txt?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200227T110838Z&X-Amz-SignedHeaders=host&X-Amz-Expires=3599&X-Amz-Credential=AKIAJKJMMSN55TF757BQ%2F20200227%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Signature=3472dc6057dd80b6b291b9a36fdb581e04c52ccd4259811ef5403f89dd46d041");

function uploadFile(){
    console.log("in upload file");
    console.log(fileUpload.files[0]);

    const formData = new FormData()
    formData.append('document', fileUpload.files[0])

    fetch('/document', {
        method: 'POST',
        body: formData
      })
      .then(response => {
        console.log(response)
      })
      .catch(error => {
        console.error(error)
      })
}

fileUpload.addEventListener('change', uploadFile, true)