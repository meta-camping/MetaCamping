import React, {useState} from 'react';

import Map from './Map';
import SelectMap from './SelectMap';
import SearchData from "./SearchData";
function Main() {
    return (
        <div>
            <div style={{display: "flex"}}>
                <Map/>
                <SearchData />
            </div>
        </div>
    )
}
export default Main;