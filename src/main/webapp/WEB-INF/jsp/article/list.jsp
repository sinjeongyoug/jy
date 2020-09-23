<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="${board.name} 게시물 리스트" />
<%@ include file="../part/head.jspf"%>

<div class="con margin-top-30">
	<span> <span>총 게시물 수 : </span> <span>${totalCount}</span>
	</span> <span>/</span> <span> <span>현재 페이지 : </span> <span
		style="color: red;">${param.page}</span>
	</span>
</div>

<div class="con">
	<form action="" name="searchForm">
		<input type="hidden" name="boardCode" value="${param.boardCode}">
		<input type="hidden" name="page" value="1">
		<select name="searchType">
			<option value="title">제목</option>
			<option value="body">내용</option>
			<option value="titleAndBody">제목+내용</option>
		</select>
		<script>
		if ( typeof param.searchType == 'undefined' ) {
			param.searchType = 'title';
		}
		
		$('form[name="searchForm"] select[name="searchType"]').val(param.searchType);
		</script>
		<input type="text" name="searchKeyword" placeholder="검색어를 입력해주세요." value="${param.searchKeyword}">
		<input type="submit" value="검색">
	</form>
</div>

<tr>
</tr>
<!-- PC용 -->
<div class="table-box con visible-on-md-up">
	<table>
		<colgroup>
			<col width="100" />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<th>번호</th>
				<th>날짜</th>
				<th>조회수</th>
				<th>제목</th>
				<th>좋아요</th>
				<th>작성자</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${articles}" var="article">
				<tr>
					<td>${article.id}</td>
					<td>${article.regDate}</td>
					<td>${article.hit}</td>
					<td><a href="${article.getDetailLink(board.code)}">${article.title}</a></td>
					<td>${article.extra.likePoint}</td>
					<td>${article.extra.writer}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<!-- URL 초기값 -->
<c:url var="pageUrl" value="${board.code}-list">
	<c:forEach items="${param}" var="entry">
		<c:if test="${entry.key != 'page'}">
			<c:param name="${entry.key}" value="${entry.value}" />
		</c:if>
	</c:forEach>
</c:url>

<div class="page-menu text-align-center margin-top-30">
	<c:if test="${needToShowPageBtnToFirst}">
		<a title="1 페이지로 이동합니다." href="${pageUrl}?page=1"> <i
			class="fas fa-angle-double-left"></i>
		</a>
	</c:if>
	<c:if test="${beforeMorePages}">
		<a title="${param.page - pageBoundSize - 1} 페이지로 이동합니다."
			href="${pageUrl}?page=${param.page - pageBoundSize - 1}"> <i
			class="fas fa-angle-left"></i>
		</a>
	</c:if>
	<c:forEach begin="${pageStartsWith}" end="${pageEndsWith}"
		var="currentPage">
		<c:if test="${currentPage == param.page}">
			<a title="${currentPage} 페이지로 이동합니다."
				style="color: red; font-weight: bold;"
				href="${pageUrl}?page=${currentPage}">${currentPage}</a>
		</c:if>
		<c:if test="${currentPage != param.page}">
			<a title="${currentPage} 페이지로 이동합니다."
				href="${pageUrl}?page=${currentPage}">${currentPage}</a>
		</c:if>
	</c:forEach>
	<c:if test="${afterMorePages}">
		<a title="${param.page + pageBoundSize + 1} 페이지로 이동합니다."
			href="${pageUrl}?page=${param.page + pageBoundSize + 1}"> <i
			class="fas fa-angle-right"></i>
		</a>
	</c:if>
	<c:if test="${needToShowPageBtnToLast}">
		<a title="${totalPage} 페이지로 이동합니다."
			href="${pageUrl}&page=${totalPage}"> <i
			class="fas fa-angle-double-right"></i>
		</a>
	</c:if>
</div>


<!-- 모바일 용 -->
<div class="table-box con visible-on-sm-down">
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>조회수</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${articles}" var="article">
				<tr>
					<td>${article.id}</td>
					<td><a href="${article.getDetailLink(board.code)}">${article.title}</a>
						<br /> 날짜 : ${article.regDate} <br /> 작성 :
						${article.extra.writer} <br /> 조회수 : ${article.hit}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>


<div class="btn-box con margin-top-20">
	<a class="btn btn-primary" href="./${board.code}-write">글쓰기</a>
</div>

<%@ include file="../part/foot.jspf"%>