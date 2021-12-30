<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eBooks Library</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

<div id="header">
    <h1>
        <a href="/" style="text-decoration: none"><i class="icon large">&#128218;</i></a>
        eBook Library - ${entry.title}
    </h1>
</div>

<div id="root">
    <h2>${entry.title}</h2>

    <div id="book">
        <div>Authors: <#list entry.authors as author>${author}</#list></div>
        <div>Pages: ${entry.pageCount}</div>
        <div>Path: <a href="${entry.downloadUrl}" target="_blank">${entry.path}</a></div>
        <div>Collection: <a href="/collection/${entry.collectionId}">${entry.collectionName}</a></div>

        <h3>Contents</h3>
        <ul>
            <#list entry.contents as contentsItem>
                <li>${contentsItem}</li>
            </#list>
        </ul>

        <h3><a href="${entry.downloadUrl}" target="_blank">${entry.path}</a></h3>
    </div>
</div>

</body>
</html>
