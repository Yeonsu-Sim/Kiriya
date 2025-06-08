<template>
  <div class="main-wrapper">
    <!-- Fireflies -->
    <div class="firefly-container">
      <div v-for="i in 70" :key="`firefly-${i}`" class="firefly" :style="generateFireflyStyle(i)"></div>
    </div>

    <!-- Single Elephant -->
    <div class="elephant-area">
      <img
        :src="'/src/assets/elephant-illustration.png'"
        class="elephant mom single-elephant"
        alt="Elephant"
      />
    </div>

    <!-- Hero Section -->
    <section class="hero-section" ref="heroSectionRef">
      <div class="parallax-bg" ref="parallaxBgRef"></div>
      <div class="hero-content" data-aos="fade-up" data-aos-duration="1000" ref="heroContentRef">
        <h1 class="hero-title">
          <span class="motion-word gradient-text" style="font-size:50px;">끼리끼리</span><br />
          <span class="motion-word gradient-text" style="font-size:60px;">뭉쳐라 여행,</span><br />
          <span class="highlighted gradient-text-secondary">Kiriya</span>에서
        </h1>
        <p class="hero-subtitle" data-aos="fade-up" data-aos-delay="400">
          끼리야가 길잡이가 되어줄게요.
        </p>
        <v-btn large class="mt-8 hero-button gradient-btn" data-aos="zoom-in" data-aos-delay="600" @click="goTo('map')">
          여행 시작하기
          <v-icon right dark>mdi-airplane-takeoff</v-icon>
        </v-btn>
      </div>
    </section>

    <!-- Card Grid Section -->
    <section class="grid-section py-16 px-6">
      <div class="card-grid">
        <div class="grid-card" v-for="(card, index) in cards" :key="card.title" @click="goTo(card.link)"
             :data-aos="index % 2 === 0 ? 'flip-left' : 'flip-right'"
             data-aos-easing="ease-out-cubic"
             data-aos-duration="800">
          <div class="card-icon-background">
            <span class="card-icon">{{ card.icon }}</span>
          </div>
          <h3>{{ card.title }}</h3>
          <p>{{ card.description }}</p>
          <div class="glow-effect"></div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import AOS from 'aos'
import 'aos/dist/aos.css'
import { onMounted, onUnmounted, ref } from 'vue'

const heroSectionRef = ref(null);
const parallaxBgRef = ref(null);
const heroContentRef = ref(null);

const cards = [
  {
    icon: '✈️',
    title: '나의 여행',
    description: '내가 만든 여정을 확인하고 관리해보세요.',
    link: 'trips'
  },
  {
    icon: '👥',
    title: '나의 그룹',
    description: '함께하는 사람들과의 기록을 보관하세요.',
    link: 'groups'
  },
  {
    icon: '📢',
    title: '공지사항',
    description: '새로운 업데이트와 주요 소식을 만나보세요.',
    link: 'support/'
  },
  {
    icon: '🔥',
    title: '핫한 장소',
    description: '사람들의 여행을 구경하고 소통해보세요.',
    link: 'feed'
  }
]

const goTo = (section) => {
  window.location.href = `/${section}`
}

const generateFireflyStyle = () => {
  const top = Math.random() * 100;
  const left = Math.random() * 100;
  const delay = Math.random() * 20;
  const duration = 10 + Math.random() * 15;
  const size = 4 + Math.random() * 4;
  return `
    top: ${top}%;
    left: ${left}%;
    width: ${size}px;
    height: ${size}px;
    animation-delay: ${delay}s;
    animation-duration: ${duration}s;
  `;
}

const handleScroll = () => {
  const scrolled = window.scrollY;
  if (parallaxBgRef.value) {
    parallaxBgRef.value.style.transform = `translateY(${scrolled * 0.2}px) scale(1.05)`;
  }
};

const handleMouseMove = (event) => {
  if (!heroSectionRef.value || !heroContentRef.value) return;

  const { clientX, clientY } = event;
  const { offsetWidth, offsetHeight } = heroSectionRef.value;

  const xPos = (clientX / offsetWidth - 0.5) * 15;
  const yPos = (clientY / offsetHeight - 0.5) * 10;

  if (heroContentRef.value) {
    heroContentRef.value.style.transform = `translate(${xPos}px, ${yPos}px)`;
  }
   if (parallaxBgRef.value) {
    parallaxBgRef.value.style.backgroundPosition = `${50 + xPos / 30}% ${50 + yPos / 30}%`;
  }
};


onMounted(() => {
  AOS.init({
    duration: 800,
    easing: 'ease-in-out-quad',
    once: false,
    offset: 50,
  })

  window.addEventListener('scroll', handleScroll);
  if (heroSectionRef.value) {
    heroSectionRef.value.addEventListener('mousemove', handleMouseMove);
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
  if (heroSectionRef.value) {
    heroSectionRef.value.removeEventListener('mousemove', handleMouseMove);
  }
})
</script>

<style scoped>
@import 'https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css';

.main-wrapper {
  overflow-x: hidden;
  position: relative;
  /*
    Unified gradient: Dark purple top for hero, transitioning smoothly
    to a lighter lavender/purple for the card section.
  */
  background: linear-gradient(180deg,
    #2a2035 0%,      /* Dark Purple Top (Hero section top) */
    #382c4d 20%,     /* Medium Dark Purple (Hero section mid) */
    #453866 40%,     /* Mid Purple (Hero section bottom / Transition start) */
    #65588B 65%,     /* Lighter Purple (Transitioning to Card section top) */
    #9A90B5 80%,     /* Even Lighter Purple (Card section mid) */
    #D8D2EB 100%     /* Lightest Lavender (Card section bottom) */
  );
  color: #F0F0F0;
}

/* Fireflies */
.firefly-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  pointer-events: none;
  z-index: 9;
  overflow: hidden;
}
.firefly {
  position: absolute;
  background: #f5ffb8;
  border-radius: 50%;
  box-shadow: 0 0 12px 3px #ffffaa, 0 0 20px 6px rgba(255, 255, 170, 0.4);
  opacity: 0;
  animation: fly 20s ease-in-out infinite alternate, fadeInOut 20s ease-in-out infinite alternate;
}

@keyframes fly {
  0%   { transform: translate(0, 0) scale(0.7); }
  25%  { transform: translate(70px, -50px) scale(1.1); }
  50%  { transform: translate(15px, 40px) scale(0.9); }
  75%  { transform: translate(-60px, -30px) scale(1.0); }
  100% { transform: translate(0, 0) scale(0.7); }
}
@keyframes fadeInOut {
  0%, 100% { opacity: 0.2; }
  10%, 90% { opacity: 0.7; }
  20%, 80% { opacity: 0.4; }
  30%, 70% { opacity: 0.9; }
  50%      { opacity: 0.3; }
}


/* Elephants */
.elephant-area {
  position: fixed;
  top: 82vh;
  left: 0;
  width: 100vw;
  height: 10vh;
  pointer-events: none;
  z-index: 8;
}
.elephant {
  position: absolute;
  opacity: 0;
  animation: elephant-float 40s linear infinite, elephant-fade 40s linear infinite;
  will-change: transform, opacity;
  filter: brightness(0.85);
}
.elephant.mom { width: 65px; }

.single-elephant {
  bottom: 5%;
  left: -100px;
  animation-delay: 0s;
}

@keyframes elephant-float {
  0%   { transform: translateX(0); }
  100% { transform: translateX(110vw); }
}
@keyframes elephant-fade {
  0% { opacity: 0; transform: translateX(0) scaleX(1); }
  10% { opacity: 0.55; }
  40% { opacity: 0.55; }
  49% { opacity: 0; transform: translateX(110vw) scaleX(1); }
  50% { opacity: 0; transform: translateX(110vw) scaleX(-1); }
  60% { opacity: 0.55; }
  90% { opacity: 0.55; }
  100% { opacity: 0; transform: translateX(0vw) scaleX(-1); }
}


/* Hero Section */
.hero-section {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-color: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  z-index: 3;
}
.parallax-bg {
  position: absolute;
  top: -10%;
  left: -10%;
  width: 120%;
  height: 120%;
  background-image: url('/images/hero-bg.jpg');
  background-size: cover;
  background-position: center center;
  z-index: 0;
  transition: transform 0.2s ease-out, background-position 0.2s ease-out;
  filter: brightness(0.5) contrast(1) saturate(0.8);
}

.hero-content {
  z-index: 2;
  color: #F0F0F0;
  position: relative;
  transition: transform 0.1s ease-out;
}
.hero-title {
  font-size: clamp(2.5rem, 6vw, 4.2rem);
  font-weight: 800;
  line-height: 1.2;
  /* Enhanced text-shadow for a "glow" effect */
  text-shadow:
    0 0 7px rgba(255, 255, 255, 0.5),   /* Inner soft white glow */
    0 0 15px rgba(220, 210, 255, 0.4), /* Slightly wider lavender glow */
    0 0 25px rgba(190, 180, 240, 0.3), /* Widest subtle purple glow */
    2px 2px 5px rgba(0, 0, 0, 0.2);     /* Subtle dark shadow for depth */
}

/* Brighter gradient text */
.gradient-text {
  /* Lighter to Light, slightly brighter */
  background: linear-gradient(135deg, #E0E1F5, #B8BADD);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-fill-color: transparent;
}
.gradient-text-secondary {
  /* Light to Primary, slightly brighter */
  background: linear-gradient(135deg, #B8BADD, #9799CF);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
   background-clip: text;
  text-fill-color: transparent;
}
.motion-word {
  display: inline-block;
  animation: floatY 2.5s ease-in-out infinite alternate;
}
.highlighted {
  animation: popIn 1s cubic-bezier(0.68, -0.55, 0.27, 1.55) 0.2s backwards;
}
@keyframes floatY {
  from { transform: translateY(-2px); }
  to { transform: translateY(-10px); }
}
@keyframes popIn {
  0% { transform: scale(0.5); opacity: 0; }
  100% { transform: scale(1); opacity: 1; }
}
.hero-subtitle {
  font-size: clamp(1rem, 2.5vw, 1.4rem);
  color: #D0D0D0;
  margin-top: 1.5rem;
  font-weight: 300;
}
.hero-button {
  background-color: #7F80BF !important;
  color: #F0F0F0 !important;
  border-radius: 25px !important;
  padding: 0 28px !important;
  height: 50px !important;
  font-weight: bold !important;
  box-shadow: 0 4px 15px rgba(127, 128, 191, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease !important;
}
.hero-button:hover {
  transform: translateY(-3px) scale(1.03);
  box-shadow: 0 6px 20px rgba(127, 128, 191, 0.4);
  background-color: #A1A3D6 !important;
}


/* Card Grid Section */
.grid-section {
  /* background-color: #e8eaf6; No longer needed, uses main-wrapper gradient */
  background-color: transparent; /* Ensures main-wrapper gradient shows through */
  position: relative;
  z-index: 10; /* Keep above animated elements if they overlap */
  overflow: hidden; /* If glow effects might exceed bounds */
  /* Text color for cards section needs to be dark for readability on lighter gradient part */
  color: #333;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(260px, 1fr));
  gap: 30px;
  max-width: 700px;
  margin: 0 auto;
  position: relative;
  z-index: 1; /* Above any potential pseudo-elements in grid-section */
}
.grid-card {
  background: rgba(255, 255, 255, 0.8); /* Slightly transparent white for a softer look on gradient */
  border-radius: 20px;
  padding: 2rem 1.8rem;
  box-shadow: 0 6px 20px rgba(127, 128, 191, 0.1); /* Adjusted shadow for new bg */
  transition: transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1), box-shadow 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  min-height: 220px;
  position: relative;
  overflow: hidden;
  border: 1px solid transparent;
}
.grid-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 15px 35px rgba(127, 128, 191, 0.18); /* Stronger shadow */
  border-color: #7F80BF;
  background: white; /* Solid white on hover for pop */
}
.grid-card:hover .glow-effect {
  opacity: 1;
  transform: scale(1.5);
}
.card-icon-background {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background-color: rgba(127, 128, 191, 0.15); /* Slightly more opaque */
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 1rem;
}
.card-icon {
  font-size: 1.8rem;
}
.grid-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.75rem;
  color: #3a304d;
  font-weight: 600;
}
.grid-card:hover h3{
  color: #7F80BF;
}
.grid-card p {
  font-size: 0.9rem;
  color: #4B4B5E; /* Slightly darker for better contrast on semi-transparent bg */
  line-height: 1.6;
  flex-grow: 1;
}
.glow-effect {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(127, 128, 191, 0.15) 0%, rgba(127, 128, 191, 0) 70%);
  opacity: 0;
  transition: opacity 0.4s ease, transform 0.4s ease;
  pointer-events: none;
  z-index: 0;
}


@media (max-width: 768px) {
  .card-grid {
    grid-template-columns: 1fr;
    gap: 20px;
    max-width: 400px;
  }
  .firefly-container { height: 60vh; }
  .elephant-area { top: 53vh; height: 10vh; }
  .hero-section { min-height: 60vh; }
  .main-wrapper {
    background: linear-gradient(180deg,
      #2a2035 0%,
      #382c4d 25%,
      #453866 50%, /* Hero area darker */
      #7b7099 70%, /* Transition for cards on mobile */
      #c1bcdf 85%,
      #e8eaf6 100%
    );
  }
}

@media (max-width: 480px) {
  .grid-card {
    padding: 1.5rem 1.2rem;
    min-height: 180px;
  }
  .card-icon-background {
    width: 50px; height: 50px;
  }
  .card-icon { font-size: 1.5rem; }
  .hero-button {
    padding: 0 20px !important;
    height: 44px !important;
  }
  .hero-title {
    font-size: clamp(2rem, 8vw, 3rem);
  }
  .hero-subtitle {
    font-size: clamp(0.9rem, 4vw, 1.1rem);
  }
  .elephant-area { top: 50vh; }
}
</style>