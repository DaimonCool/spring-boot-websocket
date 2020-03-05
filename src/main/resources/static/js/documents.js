let fileUpload = document.querySelector('#fileUpload');
let documentsBlock = document.querySelector('#documents');
let loadingFilesText = document.querySelector('#loading-files');

function downloadDocument(url) {
    document.getElementById('my_iframe').src = url;
}
//download("https://daimon-bucket.s3.us-east-2.amazonaws.com/module6.png");

async function getAllDocumentLinks() {
    let response = await fetch('/document');
    return response.json();
}

async function initializePage() {
    let links = await getAllDocumentLinks();
    for (link of links){
        let li = document.createElement("li");
        let a = document.createElement("a");
        a.href = "#";
        a.addEventListener('click', function(){
              downloadDocument(link);
        });
        let linkName = link.substring(link.lastIndexOf("/") + 1)
        a.innerHTML = linkName;
        li.appendChild(a);
        documentsBlock.appendChild(li);
    }
    loadingFilesText.style.display = 'none';
};

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
document.addEventListener('DOMContentLoaded', initializePage, true)