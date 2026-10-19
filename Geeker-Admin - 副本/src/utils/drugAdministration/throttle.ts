export function throttle(fn: any, delay: number) {
  let timer: ReturnType<typeof setTimeout>;
  let lastTime = 0;
  return function () {
    const context = null;
    const args = null;
    const nowTime = +new Date();
    if (nowTime - lastTime > delay) {
      lastTime = nowTime;
      fn.apply(context, args);
    } else {
      clearTimeout(timer);
      timer = setTimeout(() => {
        fn.apply(context, args);
      }, delay);
    }
  };
}
