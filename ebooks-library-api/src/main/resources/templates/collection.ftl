<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>eBooks Library</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

<div id="header">
    <h1><i class="icon large">&#128218;</i>eBook Library</h1>
</div>

<div id="root">

    <h2>${collection.name}</h2>

    <ul>
        <#list collection.entries as entry>
            <ul>
                <a href="${entry.downloadUrl}">
                    ${entry.title}
                </a>
            </ul>
        </#list>
    </ul>

</div>

</body>
</html>
