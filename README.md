# NAVER API 호출하기

## Reference
 - http://naver.github.io/searchad-apidoc/#/samples
 - 요기 호출하기위함

## What you should do
0. Local에 Spring 서버 띄우기
1. `X-API-KEY` Header 넣기
2. `X-API-SECRET` Header 넣기
3. `X-Customer` Header 넣기
4. `X-Signature`, `X-Timestamp`는 넣지마!!! 넣어줄게!!
5. Host는 `http://localhost:8080` 사용해!
6. 원하는 API Path(`/keywordstool`)는 그대로 사용
7. [NAVER API 가이드](http://naver.github.io/searchad-apidoc/#/operations/GET/~2Fkeywordstool) 보고 넣을 파라미터는 그대로 넣어주셈!!
8. POST MAN으로 호출 고고싱

## Example Request
```curl
curl --location --request GET 'http://localhost:8080/keywordstool?hintKeywords=퀘스트프로틴칩&showDetail=1' \
--header 'X-API-KEY: 010000dfsdf0077bc0fbaa9853289ee2c5f94912882697a19af92052382c0252fe98dffdf91' \
--header 'X-Customer: 2436212' \
--header 'X-API-SECRET: AQAAAAB3vA+6qYUyieJpu19MDLVdUMdizjPQboYYYw=='
```