import api from "../api/api";

const PREFIX = "/clinicamedagil-service";

/**
 * Marca consulta com POST /consultas e corpo { horarioId } (fluxo paciente).
 * Ajuste o import de `api` se o seu `api.ts` estiver em outro caminho.
 */
export async function marcarConsultaPorHorario(horarioId: number) {
  const { data } = await api.post(`${PREFIX}/consultas`, { horarioId });
  return data;
}
