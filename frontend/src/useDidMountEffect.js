// useEffect의 첫 렌더링 막는 함수
import { useEffect, useRef } from 'react';

const useDidMountEffect = (func, deps) => {
    const didMount = useRef(false);

    useEffect(() => {
        if (didMount.current) func();
        else didMount.current = true;
    }, deps);
};

export default useDidMountEffect;