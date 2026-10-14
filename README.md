# kdt-board

데브코스 부트캠프의 Spring Boot 게시판/API 실습 프로젝트입니다.

- Java 21, Spring Boot, Gradle
- 현재 `/hello` API 구현

## 로컬 설정

처음 내려받았다면 `src/main/resources/application-example.yaml`을 같은 폴더의
`application.yaml`로 복사합니다. 기존 파일이 있으면 그대로 사용합니다.
실제 DB 비밀번호와 API 키는 제외된 로컬 설정 또는 환경변수에만 넣습니다.
환경변수는 `${EXTERNAL_API_KEY}`처럼 참조하며 비밀값을 기본값으로 넣지 않습니다.

## 커밋과 푸시

현재 Mac에는 자동 검사가 설치되어 있습니다. 다른 곳에 clone했다면 Python 3와
Gitleaks 8.30.1 이상을 설치한 뒤 `sh scripts/install-hooks.sh`를 한 번 실행하세요.

```sh
git add .
git commit -m "update"
git push
```

커밋 시 업로드 대상 전체를, 푸시 시 전송할 브랜치의 과거 이력까지 검사합니다.
실제 설정 파일, 개인 키, 비밀번호·토큰 패턴을 발견하거나 검사 도구가 없으면 중단합니다.
오류에는 파일명·줄 번호·규칙만 표시하며 비밀값은 표시하지 않습니다.

차단되면 해당 값을 로컬 설정으로 옮기고 다시 `git add` 후 커밋하세요.
이미 커밋한 비밀은 파일에서 지우기만 해도 과거 이력에 남으므로 이력 정리가 필요합니다.
원격에 노출된 키는 폐기·재발급해야 합니다.
자동 검사가 모든 비밀을 탐지하지는 못하며, 훅을 우회하거나 웹에서 올리는 작업은 보호하지 못합니다.
