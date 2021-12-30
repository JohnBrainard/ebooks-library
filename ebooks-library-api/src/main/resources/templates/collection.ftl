<#import "layout/main.ftl" as main>
<#import "layout/components.ftl" as components>

<@main.main title="${collection.name}">
    <h2>${collection.name}</h2>

    <div id="books">
        <#list collection.entries as entry>
            <@components.collectionEntry entry=entry showAuthors=true />
        </#list>
    </div>
</@main.main>
