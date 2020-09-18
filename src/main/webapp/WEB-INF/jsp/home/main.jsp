<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="배경화면" />

<%@ include file="../part/head.jspf"%>

<div class="title-container">
  <h1 class="title"></h1>
  <h1 class="title-down"></h1>
</div>

<fieldset class="field-container">
  <input type="text" placeholder="Search..." class="field" />
  <div class="icons-container">
    <div class="icon-search"></div>
    <div class="icon-close">
      <div class="x-up"></div>
      <div class="x-down"></div>
    </div>
  </div>
</fieldset>

<h1 class="con">알파테스트 기간입니다.(v0.01)</h1>

<h2 class="con">기능 리스트</h1>

<div class="con">
	<ul>
		<li>- 회원가입</li>
		<li>- 로그인</li>
		<li>- 로그아웃</li>
		<li>- 자유게시판</li>
		<li>- 회원정보수정</li>
		<li>- 댓글 </li>
		<li>- 이미지 파일 첨부 </li>
		<li>- 동영상 파일  </li>
		<li>- 토스트에디터 적용  </li>
		<li>- 아이디찾기 </li>
		<li>- 비밀번호찾기(임시비밀번호) </li>
		<li>- 조회수 </li>
		<li>- 좋아요 기능 </li>
		<li>- 검색 기능 </li>
		<li>- 게시물 페이징 </li>		
	</ul>
</div>

<%@ include file="../part/foot.jspf"%>

