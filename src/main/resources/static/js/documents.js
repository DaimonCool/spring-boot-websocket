let fileUpload = document.querySelector('#fileUpload');
let documentsBlock = document.querySelector('#documents');
let loadingFilesText = document.querySelector('#loading-files');
let uploadBlock = document.querySelector('#uploadBlock');

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
    for (link of links) {
        addLink(link);
    }
    loadingFilesText.style.display = 'none';
}

function addLink(link) {
       let li = document.createElement("li");
       let linkName = link.substring(link.lastIndexOf("/") + 1)
       linkName = linkName.split("%20").join(" ");

       let a = "<a href='#' onclick=\"downloadDocument('" + link + "')\" >" + linkName + "</a>";
       li.innerHTML = a;
       documentsBlock.appendChild(li);
}

function uploadFile(){
    let loadingSign = document.createElement("i");
    loadingSign.className = "fa fa-circle-o-notch fa-spin";
    uploadBlock.appendChild(loadingSign);

    const formData = new FormData()
    formData.append('document', fileUpload.files[0])

    fetch('/document', {
        method: 'POST',
        body: formData
      })
      .then(response => response.text())
      .then(data => {
        addLink(data);
        loadingSign.remove();
      })
      .catch(error => {
        console.error(error)
        loadingSign.remove();
      })
}

fileUpload.addEventListener('change', uploadFile, true)
document.addEventListener('DOMContentLoaded', initializePage, true)