<template>
  <v-app>
  <v-container fluid class="auth-wrapper d-flex flex-column align-center justify-center">
    <div class="w-auto h-auto">
      <v-img
        src="@/assets/elephant-illustration.png"
        class="illustration pa-0 ma-0"
      />
    </div>
    <v-card class="glass-card auth-card">
      <!-- Illustration Point -->

    <!-- Tabs -->
    <v-tabs v-model="activeTab">
      <v-tab value="login">로그인</v-tab>
      <v-tab value="register">회원가입</v-tab>
    </v-tabs>
    <v-divider />

    <v-card-text>
      <!-- Login Form -->
      <div v-if="activeTab === 'login'">
        <v-form @submit.prevent="onLogin">
          <v-text-field
            label="이메일"
            v-model="loginForm.email"
            prepend-icon="mdi-email"
            :rules="emailRules"
            required
          />
          <v-text-field
            type="password"
            label="비밀번호"
            v-model="loginForm.password"
            prepend-icon="mdi-lock"
            required
          />
          <v-checkbox v-model="loginForm.rememberMe" label="아이디 기억하기" />

          <v-btn
          type="submit"
          class="gradient-btn w-100"
          :loading="loading"
          >
          로그인
          </v-btn>
        </v-form>

        <!-- 비밀번호 찾기 링크 -->
         <v-card-actions class="justify-center"><span class="text-caption">비밀번호를 잊어버리셨나요?</span>
          <RouterLink class="router-link" to="/member/find-password">비밀번호 찾기</RouterLink>
        </v-card-actions>
      </div>
    
    <!-- Register with Stepper -->
    <div v-else>
      <v-stepper
      v-model="registerStep"
      alt-labels
      :items="['약관 동의','정보 입력','완료']"
    >
      <!-- 1단계: 약관 동의 -->
      <template #item.1>
        <v-card flat class="pa-2">
          <v-textarea
          class="privacy-text"
          :value="privacyText"
          readonly
          auto-grow
          hide-details
          outlined
          rows="10"
          />
          <v-checkbox
            v-model="registerForm.agree"
            label="개인정보처리동의 내용에 동의합니다"
            class="mb-6"
          />

        </v-card>
      </template>

      <!-- 2단계: 정보 입력 -->
      <template #item.2>
        <v-card flat class="pa-6">
          <v-form ref="registerFormRef" class="space-y-4">
            <v-text-field
              v-model="registerForm.name"
              label="이름"
              prepend-icon="mdi-account"
              required
            />
            <v-text-field
              v-model="registerForm.email"
              label="이메일"
              prepend-icon="mdi-email"
              :rules="emailRules"
              required
            />
            <v-text-field
              v-model="registerForm.password"
              type="password"
              label="비밀번호"
              prepend-icon="mdi-lock"
              required
            />
            <v-text-field
              v-model="registerForm.passwordConfirm"
              type="password"
              label="비밀번호 확인"
              prepend-icon="mdi-lock-check"
              required
            />
            <v-radio-group
              v-model.number="registerForm.role"
              row
              label="회원 등급"
            >
              <v-radio label="일반 회원" :value="2"/>
              <v-radio label="관리자"     :value="1"/>
            </v-radio-group>
          </v-form>        
        </v-card>
      </template>

      <!-- 3단계: 완료 -->
      <template #item.3>
        <v-card flat class="pa-6 text-center">
          <v-icon size="64" color="success">mdi-check-circle-outline</v-icon>
          <p class="headline my-4">회원가입이 완료되었습니다!</p>
          <v-btn color="primary" @click="goToLogin">로그인으로 이동</v-btn>
        </v-card>
      </template>

       <template #actions="{ prev, next }">
        <div v-if="registerStep < 3" class="d-flex justify-space-evenly pb-3">
            <v-btn v-if="registerStep > 1"
              text
              class = "gradient-btn"
              :disable="registerStep===1"
              @click="registerStep>1 && prev()">이전</v-btn>
            <!-- 유효하지 않으면 disabled, 클릭 시 onNext2 호출 -->
            <v-btn
              text
              class = "gradient-btn"
              :disabled="(registerStep===1 && !registerForm.agree) || (registerStep===2 && !isStep2Valid)"
              @click="onNext(next)"
            >
              다음
            </v-btn>
            </div>
            
          </template>  
    </v-stepper>
    </div>
  </v-card-text>
  </v-card>
</v-container>
</v-app>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 탭 상태
const activeTab = ref(route.meta.authMode || 'login')

// route 바뀔 때마다 activeTab 업데이트
watch(() => route.meta.authMode, (mode) => { activeTab.value = mode })
const loading = ref(false)

// 이메일 정규표현식으로 검사하는 함수
const isEmail = v =>
  /^(?:[a-zA-Z0-9._%+-]+)@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}$/.test(v) ||
  '유효한 이메일 주소를 입력하세요.'

const isRequired = v => !!v || '필수 입력 항목입니다.'

const emailRules = [ isRequired, isEmail ]

// 로그인 폼 데이터
const loginForm = ref({
  email: localStorage.getItem('rememberEmail') || '',
  password: '',
  rememberMe: localStorage.getItem('rememberMe') === 'true'
})

// 로그인 처리
async function onLogin() {

  if (!loginForm.value.email || !loginForm.value.password) return
  loading.value = true
  try {
    await userStore.login({
      email: loginForm.value.email,
      password: loginForm.value.password,
      rememberMe: loginForm.value.rememberMe
    })
    router.push('/')
  } catch (err) {
    console.error(err)
    alert(err.response?.data?.message || '로그인 중 오류가 발생했습니다.')
  } finally {
    loading.value = false
  }
}

// 완료 후 로그인 이동 및 초기화
function goToLogin() {
  activeTab.value = 'login'
  loading.value = false
}

// 회원가입 스텝/데이터
const registerStep = ref(1)
const registerForm = ref({
  agree: false,
  name: '',
  email: '',
  password: '',
  passwordConfirm: '',
  role: 2
})

// 2단계 유효성 검사
const isStep2Valid = computed(() => {
  const { name, email, password, passwordConfirm, role } = registerForm.value
  return (
    name.trim() !== '' &&
    email.trim() !== '' &&
    isEmail(email) === true &&
    password !== '' &&
    passwordConfirm !== '' &&
    password === passwordConfirm &&
    role !== ''
  )
})

// 탭 변경 시 회원가입 초기화
watch(activeTab, (newTab) => {
  if (newTab === 'register') resetRegister()
})

// 단계 이동
const onNext = async(origNext) => {
  if (registerStep.value === 1) {
    origNext()
    return
  }
  else if (registerStep.value === 2) {
    
    if (!isStep2Valid.value) return
  
    loading.value = true
    try {
      await userStore.signUp({
         name: registerForm.value.name,
         email: registerForm.value.email,
         password: registerForm.value.password,
         passwordConfirm: registerForm.value.passwordConfirm,
         role: registerForm.value.role
      })
      origNext()
    } catch (err) {
      console.error(err)
      alert(err.response?.data?.message || '회원가입 중 오류가 발생했습니다.')
    } finally {
      loading.value = false
    }
  } else {
    if (registerStep.value < 3) registerStep.value++
  }
}
const prevStep = () => registerStep.value > 1 && registerStep.value--


// 회원가입 초기화
function resetRegister() {
  registerStep.value = 1
  Object.assign(registerForm.value, { agree: false, name: '', email: '', password: '', passwordConfirm: '' })
}

const privacyText = `
개인정보처리동의서

끼리야(이하 '회사'라고 합니다)는 개인정보보호법 등 관련 법령상의 개인정보보호 규정을 준수하며 귀하의 개인정보보호에 최선을 다하고 있습니다. 회사는 개인정보보호법에 근거하여 다음과 같은 내용으로 개인정보를 수집 및 처리하고자 합니다.

다음의 내용을 자세히 읽어보시고 모든 내용을 이해하신 후에 동의 여부를 결정해주시기 바랍니다.

제1조(회원 가입을 위한 정보)
회사는 이용자의 회사 서비스에 대한 회원가입을 위하여 다음과 같은 정보를 수집합니다.
[필수 수집 정보] : 이메일 주소, 비밀번호 및 이름

제2조(본인 인증을 위한 정보)
회사는 이용자의 본인 인증을 위하여 다음과 같은 정보를 수집합니다.
[필수 수집 정보] : 이메일 주소 및 이름

제3조(회사 서비스 제공을 위한 정보)
회사는 이용자에게 회사의 서비스를 제공하기 위하여 다음과 같은 정보를 수집합니다.
[필수 수집 정보] : 아이디, 이메일 주소 및 이름

제4조(서비스 이용 및 부정 이용 확인을 위한 정보)
회사는 이용자의 서비스 이용에 따른 통계·분석 및 부정이용 확인·분석을 위하여 다음과 같은 정보를 수집합니다. (부정이용이란 회원탈퇴 후 재가입, 상품 구매 후 구매취소 등을 반복적으로 행하는 등 회사가 제공하는 할인쿠폰, 이벤트 혜택 등의 경제적 이득·불법편법적으로 취득하는 행위, 이용약관 등에 금지하고 있는 행위, 명의도용 등의 불·편법행위를 말합니다.)
[필수 수집 정보] : 서비스 이용기록, 쿠키 및 접속기기 정보

제5조(개인정보 보유 및 이용 기간)
1. 수집한 개인정보는 수집·이용 동의일로부터 회원탈퇴 시까지 보관 및 이용합니다.
2. 개인정보 보유기간이 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체 없이 해당 개인정보를 파기합니다.

제6조(동의 거부 관리)
귀하는 본 안내에 따른 개인정보 수집·이용에 대하여 동의를 거부할 권리가 있습니다. 다만, 귀하가 개인정보 동의를 거부하시는 경우 서비스 이용 중 일부 제약이 발생할 수 있음을 알려드립니다.

본인은 위의 동의서 내용을 충분히 숙지하였으며, 위와 같이 개인정보를 수집·이용하는데 동의합니다.

`
</script>

<style lang="scss" scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  // height: 100vh;
  width: 600px;
  padding: 16px;
}
.auth-card {
  width: 500px;
  max-width: 90%;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(12px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  padding: 24px 16px;
  position: relative;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;

  j

  .v-tabs {
    margin-bottom: 16px;
    .v-tab { font-weight: 500; text-transform: none; color: #7f80bf; }
    .v-tab--active { color: #46266c; }
  }
  > .v-divider { margin-bottom: 16px; }
}


.illustration {
  z-index: 1;
  height:100px;
  width:100px;
}


.completion-msg { text-align: center; margin-bottom: 16px;
  p { margin: 8px 0 0; font-size: 16px; font-weight: 500; }
}

.stepper-header { display: flex; align-items: center; justify-content: space-evenly;}

.v-textarea {
  /* 브라우저 기본 폰트로 리셋 */
  font-family: initial !important;
  /* 필요하다면 크기도 조절 */
  font-size: 1rem !important;
}
</style>