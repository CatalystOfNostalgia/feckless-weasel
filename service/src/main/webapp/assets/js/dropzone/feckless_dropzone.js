// Get the template HTML and remove it from the document 
var previewNode = document.querySelector("#template");
previewNode.id = "";
var previewTemplate = previewNode.parentNode.innerHTML;
previewNode.parentNode.removeChild(previewNode);
var classID = ""
var drop = new Dropzone(document.body, {
    autoProcessQueue: false,
    autoQueue: false,
    url: "/servlet/file/upload",
    thumbnailWidth: 150,
    thumbnailHeight: 150,
    maxFiles: 1,
    paramName: "file",
    parallelUploads: 100,
    previewTemplate: previewTemplate,
    previewContainer: "#previews",
    uploadMultiple: true,
    acceptedFiles: "image/*, application/pdf, application/doc, applicatoin/docx",
});

drop.on("init", function(){
    this.element.querySelector("button[type=submit]").addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();
        drop.processQueue();
    });
});

drop.on("maxfilesexceeded", function(file){
    drop.removeFile(file);
});

drop.on("addedfile", function(file) {
    file.previewElement.querySelector("button[type=submit]").addEventListener("click", function(e) {
        drop.enqueueFile(file);
        e.preventDefault();
        e.stopPropagation();
        drop.processQueue();
    });
});

drop.on("sending", function(file, xhr, formData){
    formData.append("title", this.element.querySelector("#title").value);
    formData.append("description", this.element.querySelector("#description").value);
    formData.append("class", this.element.querySelector("#classID").value);
    classID = this.element.querySelector("#classID").value;
});

drop.on("success", function(file, response){
    window.location.href = "/course/index.jsp?cid=" + classID;
});
