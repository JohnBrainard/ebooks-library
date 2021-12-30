<#macro collectionEntry entry showCollection=false showAuthors=false>
    <div class="book">
        <span class="title">
            <a href="${entry.downloadUrl}" target="_blank">
                <i class="icon-sm">&#x1F4D6;</i>
            </a>
            <a href="/collection/${entry.collectionId}/entries/${entry.id}">
                 ${entry.title}
            </a>
        </span>
        <span class="pageCount">${entry.pageCount} pages</span>
        <#if showCollection>
            <span class="fileName">
                <a href="${entry.collectionUrl}">
                    ${entry.collectionName}
                </a>
            </span>
        </#if>
        <#if showAuthors>
            <span class="authors">
                <#list entry.authors as author>${author}</#list>
            </span>
        </#if>
    </div>
</#macro>
