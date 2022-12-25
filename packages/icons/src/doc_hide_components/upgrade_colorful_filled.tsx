/**
 * APITable <https://github.com/apitable/apitable>
 * Copyright (C) 2022 APITable Ltd. <https://apitable.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/* eslint-disable max-len */
import React from 'react';
import { makeIcon, IIconProps } from '../utils/icon';

export const UpgradeColorfulFilled: React.FC<IIconProps> = makeIcon({
  Path: ({ colors }) => <>
    <path d="M9 7L3 13" stroke={ colors[0] } strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M12.0879 6.55134C12.4603 6.73452 12.4603 7.26548 12.0879 7.44866L10.8514 8.05682C10.7033 8.12967 10.6007 8.27089 10.5772 8.43425L10.3809 9.79808C10.3218 10.2089 9.8168 10.373 9.5275 10.0754L8.56703 9.0874C8.45199 8.96906 8.28596 8.91512 8.12334 8.94323L6.76559 9.17797C6.35663 9.24867 6.04454 8.81912 6.23816 8.45202L6.88097 7.23326C6.95796 7.08728 6.95796 6.91272 6.88097 6.76674L6.23816 5.54798C6.04454 5.18088 6.35663 4.75133 6.76559 4.82203L8.12334 5.05676C8.28596 5.08488 8.45199 5.03094 8.56703 4.9126L9.5275 3.92463C9.8168 3.62705 10.3218 3.79112 10.3809 4.20192L10.5772 5.56576C10.6007 5.72911 10.7033 5.87033 10.8514 5.94318L12.0879 6.55134Z" fill={ colors[1] }/>
    <path d="M7.00003 1C7.00003 1.55228 6.55232 2 6.00003 2C5.44775 2 5.00003 1.55228 5.00003 1C5.00003 0.447715 5.44775 0 6.00003 0C6.55232 0 7.00003 0.447715 7.00003 1Z" fill={ colors[2] }/>
    <path d="M14 3C14.5523 3 15 2.55228 15 2C15 1.44772 14.5523 1 14 1C13.4477 1 13 1.44772 13 2C13 2.55228 13.4477 3 14 3Z" fill={ colors[2] }/>
    <path d="M15 12C15.5523 12 16 11.5523 16 11C16 10.4477 15.5523 10 15 10C14.4477 10 14 10.4477 14 11C14 11.5523 14.4477 12 15 12Z" fill={ colors[2] }/>
    <path d="M12.3861 9.50479C12.8003 9.53236 13.0657 9.07254 12.8348 8.72769L11.6984 7.03064C11.6065 6.89351 11.5883 6.7199 11.6496 6.56667L12.4084 4.67043C12.5626 4.2851 12.2073 3.89052 11.8079 4.00359L9.84278 4.55999C9.68398 4.60495 9.51323 4.56866 9.38645 4.463L7.81749 3.15539C7.49867 2.88967 7.01361 3.10564 6.99774 3.52036L6.91965 5.56129C6.91334 5.72621 6.82605 5.87739 6.68639 5.96531L4.95794 7.05341C4.60671 7.27451 4.66221 7.80256 5.05174 7.94582L6.96864 8.65077C7.12353 8.70774 7.24034 8.83746 7.2808 8.99746L7.78153 10.9776C7.88328 11.3799 8.40263 11.4903 8.65924 11.1641L9.92205 9.55888C10.0241 9.42917 10.1836 9.35816 10.3482 9.36913L12.3861 9.50479Z" fill={ colors[2] }/>

  </>,
  name: 'upgrade_colorful_filled',
  defaultColors: ['#FFC95C', '#FFE5B7', '#FFEB3A'],
  colorful: true,
  allPathData: ['M9 7L3 13', 'M12.0879 6.55134C12.4603 6.73452 12.4603 7.26548 12.0879 7.44866L10.8514 8.05682C10.7033 8.12967 10.6007 8.27089 10.5772 8.43425L10.3809 9.79808C10.3218 10.2089 9.8168 10.373 9.5275 10.0754L8.56703 9.0874C8.45199 8.96906 8.28596 8.91512 8.12334 8.94323L6.76559 9.17797C6.35663 9.24867 6.04454 8.81912 6.23816 8.45202L6.88097 7.23326C6.95796 7.08728 6.95796 6.91272 6.88097 6.76674L6.23816 5.54798C6.04454 5.18088 6.35663 4.75133 6.76559 4.82203L8.12334 5.05676C8.28596 5.08488 8.45199 5.03094 8.56703 4.9126L9.5275 3.92463C9.8168 3.62705 10.3218 3.79112 10.3809 4.20192L10.5772 5.56576C10.6007 5.72911 10.7033 5.87033 10.8514 5.94318L12.0879 6.55134Z', 'M7.00003 1C7.00003 1.55228 6.55232 2 6.00003 2C5.44775 2 5.00003 1.55228 5.00003 1C5.00003 0.447715 5.44775 0 6.00003 0C6.55232 0 7.00003 0.447715 7.00003 1Z', 'M14 3C14.5523 3 15 2.55228 15 2C15 1.44772 14.5523 1 14 1C13.4477 1 13 1.44772 13 2C13 2.55228 13.4477 3 14 3Z', 'M15 12C15.5523 12 16 11.5523 16 11C16 10.4477 15.5523 10 15 10C14.4477 10 14 10.4477 14 11C14 11.5523 14.4477 12 15 12Z', 'M12.3861 9.50479C12.8003 9.53236 13.0657 9.07254 12.8348 8.72769L11.6984 7.03064C11.6065 6.89351 11.5883 6.7199 11.6496 6.56667L12.4084 4.67043C12.5626 4.2851 12.2073 3.89052 11.8079 4.00359L9.84278 4.55999C9.68398 4.60495 9.51323 4.56866 9.38645 4.463L7.81749 3.15539C7.49867 2.88967 7.01361 3.10564 6.99774 3.52036L6.91965 5.56129C6.91334 5.72621 6.82605 5.87739 6.68639 5.96531L4.95794 7.05341C4.60671 7.27451 4.66221 7.80256 5.05174 7.94582L6.96864 8.65077C7.12353 8.70774 7.24034 8.83746 7.2808 8.99746L7.78153 10.9776C7.88328 11.3799 8.40263 11.4903 8.65924 11.1641L9.92205 9.55888C10.0241 9.42917 10.1836 9.35816 10.3482 9.36913L12.3861 9.50479Z'],
  width: '16',
  height: '16',
  viewBox: '0 0 16 16',
});
