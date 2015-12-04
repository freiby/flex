<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<div id="page-wrapper" class="gray-bg dashbard-1" style="min-height: 524px;">

    <div class="row border-bottom">
        <tiles:insertAttribute name="pageHeader" />
    </div>

    <tiles:insertAttribute name="pageContainer" />
    <div class="footer">
        <div class="pull-right">
            10GB of <strong>250GB</strong> Free.
        </div>
        <div>
            <strong>Copyright</strong> Example Company © 2014-2015
        </div>
    </div>
</div>