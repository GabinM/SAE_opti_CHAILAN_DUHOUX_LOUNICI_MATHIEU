import java.math.*;

public class Colors {
    /*
     * Copyright (C) 2014 Vincent Vansuyt
     *
     * This program is free software: you can redistribute it and/or modify
     * it under the terms of the GNU General Public License as published by
     * the Free Software Foundation; either version 3 of the License, or
     * (at your option) any later version.
     *
     * This program is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     * GNU General Public License for more details.
     *
     * You should have received a copy of the GNU General Public License
     * along with this program.  If not, see <http://www.gnu.org/licenses/>.
     */


    /**
     * English description :
     *   Conversions class between RGB and Lab space from this document :
     *   http://www.tsi.telecom-paristech.fr/pages/enseignement/ressources/beti/RVB_ou_LAB/html/colorspace.html
     *
     * Description en français :
     *   Classe de conversion entre les espaces de couleur RGB et Lab à partir de ce document de spécifications :
     *   http://www.tsi.telecom-paristech.fr/pages/enseignement/ressources/beti/RVB_ou_LAB/html/colorspace.html
     * @author Vincent Vansuyt
     */
        // Fonction utilisée dans convertRGB_to_Lab
        public static double f_to_Lab( double t ) {
            double seuil_Lab = 0.008856;
            double v;
            if ( t > seuil_Lab ) {
                v = Math.pow( t, ( 1.0 / 3.0 ));
            }
            else {
                v = 7.7787 * t + 16.0 / 116.0;
            }
            return v;
        }// f_to_Lab


        // Convertit le pixel RGB en composantes Lab
        // Recoit un tableau de 3 entiers R, G, B
        // Renvoie un tableau de 3 doubles L, a, b
        public static double[] convertRGB_to_Lab( int clRGB[] ) {
		/*---------- CODE SCILAB - debut ----------
		 *matRGB_to_XYZ = [ 0.618, 0.177, 0.205; ...
					   # 0.299, 0.587, 0.114; ...
					   # 0.0  , 0.056, 0.944 ];
		* clRGB_blanc = [ 255, 255, 255 ];
		* clXYZ_blanc = matRGB_to_XYZ * clRGB_blanc';
		* seuil_Lab = 0.008856;
		* clXYZ = matRGB_to_XYZ * clRGB';
		* printf("Couleur XYZ : %.3f, %.3f, %.3f\n", clXYZ( 1 ), clXYZ( 2 ), clXYZ( 3 ));
		* XsXn = clXYZ(1) / clXYZ_blanc(1);
		* YsYn = clXYZ(2) / clXYZ_blanc(2);
		* ZsZn = clXYZ(3) / clXYZ_blanc(3);
		* L = 116.0 * ( YsYn ^ ( 1.0 / 3.0 )) - 16.0;
		* if YsYn <= seuil_Lab then
		*	L = 903.3 * YsYn;
		* end
		* a = 500.0 * ( f_to_Lab( XsXn ) - f_to_Lab( YsYn ));
		* b = 200.0 * ( f_to_Lab( YsYn ) - f_to_Lab( ZsZn ));
		* clLab = [ L, a, b ]
		*---------- CODE SCILAB - fin ----------*/
            double L;
            double a;
            double b;
            double[] clRGB_blanc = { 255, 255, 255 };
            double[] clXYZ_blanc = { 0, 0, 0 };
            clXYZ_blanc[ 0 ] = 0.618 * clRGB_blanc[ 0 ] + 0.177 * clRGB_blanc[ 1 ] + 0.205 * clRGB_blanc[ 2 ];
            clXYZ_blanc[ 1 ] = 0.299 * clRGB_blanc[ 0 ] + 0.587 * clRGB_blanc[ 1 ] + 0.114 * clRGB_blanc[ 2 ];
            clXYZ_blanc[ 2 ] = 0.000 * clRGB_blanc[ 0 ] + 0.056 * clRGB_blanc[ 1 ] + 0.944 * clRGB_blanc[ 2 ];
            final double seuil_Lab = 0.008856;
            double[] clXYZ = { 0, 0, 0 };
            clXYZ[ 0 ] = 0.618 * clRGB[ 0 ] + 0.177 * clRGB[ 1 ] + 0.205 * clRGB[ 2 ];
            clXYZ[ 1 ] = 0.299 * clRGB[ 0 ] + 0.587 * clRGB[ 1 ] + 0.114 * clRGB[ 2 ];
            clXYZ[ 2 ] = 0.000 * clRGB[ 0 ] + 0.056 * clRGB[ 1 ] + 0.944 * clRGB[ 2 ];
            double XsXn = clXYZ[ 0 ] / clXYZ_blanc[ 0 ];
            double YsYn = clXYZ[ 1 ] / clXYZ_blanc[ 1 ];
            double ZsZn = clXYZ[ 2 ] / clXYZ_blanc[ 2 ];
            L = 116.0 * ( Math.pow( Math.abs( YsYn ), ( 1.0 / 3.0 )) ) - 16.0;
            if ( YsYn <= seuil_Lab ) {
                L = 903.3 * YsYn;
            }
            a = 500.0 * ( f_to_Lab( XsXn ) - f_to_Lab( YsYn ));
            b = 200.0 * ( f_to_Lab( YsYn ) - f_to_Lab( ZsZn ));
            double[] clLab = { L , a, b };
            return clLab;
        }// convertRGB_to_Lab


        // Fonction utilisée par convertLab_to_RGB
        private static double f_to_XYZ( double t ) {
            double v = Math.pow( t , 3);
            if ( t <= 0.207 ) {
                v = ( 116.0 * t - 16.0 ) / 903.3;
            }
            return v;
        }// f_to_XYZ


        // Convertit le pixel Lab en composantes RGB
        // Recoit un tableau de 3 doubles L, a, b
        // Renvoie un tableau de 3 entiers R, G, B
        public static int[] convertLab_to_RGB( double[] clLab ) {
            /*
             * ---------- CODE SCILAB - debut ----------
             *	matRGB_to_XYZ = [ 0.618, 0.177, 0.205; ...
             *               0.299, 0.587, 0.114; ...
             *               0.0  , 0.056, 0.944 ];
             * clRGB_blanc = [ 255, 255, 255 ];
             * clXYZ_blanc = matRGB_to_XYZ * clRGB_blanc';
             * Xn = clXYZ_blanc( 1 );
             * Yn = clXYZ_blanc( 2 );
             * Zn = clXYZ_blanc( 3 );
             * L = clLab( 1 );
             * a = clLab( 2 );
             * b = clLab( 3 );
             * Y = Yn * L / 903.3;
             * if L > 8.0 then
             *	Y = Yn * (( L + 16.0 ) / 116.0 )^3;
             * end
             * X = Xn * f_to_XYZ( a / 500.0 + (( L + 16.0 ) / 116.0) );
             * Z = Zn * f_to_XYZ( (( L + 16.0 ) / 116.0) - b / 200.0 );
             * printf("Couleur XYZ : %.3f, %.3f, %.3f\n", X, Y, Z);
             * matXYZ_to_RGB = [ 1.876, -0.533, -0.343; ...
             *                   -0.967, 1.998, -0.031; ...
             * 0.057, -0.118, 1.061];
             * clRGB = matXYZ_to_RGB * [ X; Y; Z ];
             *---------- CODE SCILAB - fin ---------- */
            double[] clRGB_blanc = { 255, 255, 255 };
            double[] clXYZ_blanc = { 0, 0, 0 };
            clXYZ_blanc[ 0 ] = 0.618 * clRGB_blanc[ 0 ] + 0.177 * clRGB_blanc[ 1 ] + 0.205 * clRGB_blanc[ 2 ];
            clXYZ_blanc[ 1 ] = 0.299 * clRGB_blanc[ 0 ] + 0.587 * clRGB_blanc[ 1 ] + 0.114 * clRGB_blanc[ 2 ];
            clXYZ_blanc[ 2 ] = 0.000 * clRGB_blanc[ 0 ] + 0.056 * clRGB_blanc[ 1 ] + 0.944 * clRGB_blanc[ 2 ];
            double Xn = clXYZ_blanc[ 0 ];
            double Yn = clXYZ_blanc[ 1 ];
            double Zn = clXYZ_blanc[ 2 ];
            final double seuil_Lab = 0.008856;
            double L = clLab[ 0 ];
            double a = clLab[ 1 ];
            double b = clLab[ 2 ];
            double Y = Yn * L / 903.3;
            if ( L > 8.0 ) {
                Y = Yn * Math.pow(( L + 16.0 ) / 116.0 , 3 );
            }
            double X = Xn * f_to_XYZ( a / 500.0 + (( L + 16.0 ) / 116.0) );
            double Z = Zn * f_to_XYZ( (( L + 16.0 ) / 116.0) - b / 200.0 );
            double R = 1.876 * X -0.533 * Y -0.343 * Z;
            double G = -0.967 * X + 1.998 * Y -0.031 * Z;
            double B = 0.057 * X -0.118 * Y + 1.061 * Z;
            int iR = Math.max( 0, (int)Math.round( R ) );
            int iG = Math.max( 0, (int)Math.round( G ) );
            int iB = Math.max( 0, (int)Math.round( B ) );
            int[] tabRGB = new int[ 3 ];
            tabRGB[ 0 ] = Math.min( iR, 255);
            tabRGB[ 1 ] = Math.min( iG, 255);
            tabRGB[ 2 ] = Math.min( iB, 255);
            return tabRGB;
        };// convertLab_to_RGB
};// Colors

