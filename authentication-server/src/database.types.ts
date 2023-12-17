export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export interface Database {
  custom_auth: {
    Tables: {
      refresh_token: {
        Row: {
          fk_session_id: string | null
          id: number
          revoked: boolean | null
          token: string
        }
        Insert: {
          fk_session_id?: string | null
          id?: number
          revoked?: boolean | null
          token: string
        }
        Update: {
          fk_session_id?: string | null
          id?: number
          revoked?: boolean | null
          token?: string
        }
        Relationships: [
          {
            foreignKeyName: "refresh_token_fk_session_id_fkey"
            columns: ["fk_session_id"]
            isOneToOne: false
            referencedRelation: "session"
            referencedColumns: ["id"]
          }
        ]
      }
      session: {
        Row: {
          access_token: string
          access_token_expiry: string | null
          fk_user_type: number
          id: string
          session_expiry: string | null
          user_id: string
        }
        Insert: {
          access_token: string
          access_token_expiry?: string | null
          fk_user_type: number
          id?: string
          session_expiry?: string | null
          user_id: string
        }
        Update: {
          access_token?: string
          access_token_expiry?: string | null
          fk_user_type?: number
          id?: string
          session_expiry?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "session_fk_user_type_fkey"
            columns: ["fk_user_type"]
            isOneToOne: false
            referencedRelation: "user_type"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "session_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "person"
            referencedColumns: ["id"]
          }
        ]
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      authenticate: {
        Args: Record<PropertyKey, never>
        Returns: undefined
      }
      find_session: {
        Args: {
          session_id: string
          access_token: string
        }
        Returns: {
          session_user_id: string
          session_user_type: string
        }[]
      }
      update_session_token: {
        Args: {
          session_id: string
          new_access_token: string
        }
        Returns: string
      }
    }
    Enums: {
      [_ in never]: never
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
  public: {
    Tables: {
      appointment: {
        Row: {
          datetime: string | null
          fk_appointment_type_id: number
          fk_doctor_id: string
          fk_patient_id: string | null
          hour: string | null
          id: number
          reason: string | null
        }
        Insert: {
          datetime?: string | null
          fk_appointment_type_id: number
          fk_doctor_id: string
          fk_patient_id?: string | null
          hour?: string | null
          id?: number
          reason?: string | null
        }
        Update: {
          datetime?: string | null
          fk_appointment_type_id?: number
          fk_doctor_id?: string
          fk_patient_id?: string | null
          hour?: string | null
          id?: number
          reason?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "appointment_fk_appointment_type_id_fkey"
            columns: ["fk_appointment_type_id"]
            isOneToOne: false
            referencedRelation: "appointment_type"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "appointment_fk_doctor_id_fkey"
            columns: ["fk_doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "appointment_fk_patient_id_fkey"
            columns: ["fk_patient_id"]
            isOneToOne: false
            referencedRelation: "patient"
            referencedColumns: ["id"]
          }
        ]
      }
      appointment_has_diagnosis: {
        Row: {
          fk_appointment_id: number
          fk_diagnosis_id: number
        }
        Insert: {
          fk_appointment_id: number
          fk_diagnosis_id: number
        }
        Update: {
          fk_appointment_id?: number
          fk_diagnosis_id?: number
        }
        Relationships: [
          {
            foreignKeyName: "appointment_has_diagnosis_fk_appointment_id_fkey"
            columns: ["fk_appointment_id"]
            isOneToOne: false
            referencedRelation: "appointment"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "appointment_has_diagnosis_fk_diagnosis_id_fkey"
            columns: ["fk_diagnosis_id"]
            isOneToOne: false
            referencedRelation: "diagnosis"
            referencedColumns: ["id"]
          }
        ]
      }
      appointment_type: {
        Row: {
          id: number
          name: string
        }
        Insert: {
          id?: number
          name: string
        }
        Update: {
          id?: number
          name?: string
        }
        Relationships: []
      }
      diagnosis: {
        Row: {
          fk_doctor_id: string | null
          id: number
        }
        Insert: {
          fk_doctor_id?: string | null
          id?: number
        }
        Update: {
          fk_doctor_id?: string | null
          id?: number
        }
        Relationships: [
          {
            foreignKeyName: "diagnosis_fk_doctor_id_fkey"
            columns: ["fk_doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          }
        ]
      }
      disease: {
        Row: {
          chronic: boolean
          hereditary: boolean
          id: number
          name: string
        }
        Insert: {
          chronic?: boolean
          hereditary?: boolean
          id?: number
          name: string
        }
        Update: {
          chronic?: boolean
          hereditary?: boolean
          id?: number
          name?: string
        }
        Relationships: []
      }
      doctor: {
        Row: {
          id: string
        }
        Insert: {
          id: string
        }
        Update: {
          id?: string
        }
        Relationships: [
          {
            foreignKeyName: "doctor_id_fkey"
            columns: ["id"]
            isOneToOne: true
            referencedRelation: "person"
            referencedColumns: ["id"]
          }
        ]
      }
      doctor_performs_surgery: {
        Row: {
          fk_doctor_id: string
          fk_surgery_id: number
        }
        Insert: {
          fk_doctor_id: string
          fk_surgery_id: number
        }
        Update: {
          fk_doctor_id?: string
          fk_surgery_id?: number
        }
        Relationships: [
          {
            foreignKeyName: "doctor_performs_surgery_fk_doctor_id_fkey"
            columns: ["fk_doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "doctor_performs_surgery_fk_surgery_id_fkey"
            columns: ["fk_surgery_id"]
            isOneToOne: false
            referencedRelation: "surgery"
            referencedColumns: ["id"]
          }
        ]
      }
      doctor_schedule: {
        Row: {
          day_of_week: string | null
          doctor_id: string | null
          end_time: string | null
          schedule_id: number
          start_time: string | null
        }
        Insert: {
          day_of_week?: string | null
          doctor_id?: string | null
          end_time?: string | null
          schedule_id?: number
          start_time?: string | null
        }
        Update: {
          day_of_week?: string | null
          doctor_id?: string | null
          end_time?: string | null
          schedule_id?: number
          start_time?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "doctor_schedule_doctor_id_fkey"
            columns: ["doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          }
        ]
      }
      dose: {
        Row: {
          id: number
          timestamp: string
        }
        Insert: {
          id?: number
          timestamp: string
        }
        Update: {
          id?: number
          timestamp?: string
        }
        Relationships: []
      }
      message: {
        Row: {
          date: string | null
          doctor_read: boolean | null
          fk_doctor_id: string | null
          fk_patient_id: string | null
          id: number
          message: string | null
          patient_read: boolean | null
          patient_sent: boolean | null
        }
        Insert: {
          date?: string | null
          doctor_read?: boolean | null
          fk_doctor_id?: string | null
          fk_patient_id?: string | null
          id?: number
          message?: string | null
          patient_read?: boolean | null
          patient_sent?: boolean | null
        }
        Update: {
          date?: string | null
          doctor_read?: boolean | null
          fk_doctor_id?: string | null
          fk_patient_id?: string | null
          id?: number
          message?: string | null
          patient_read?: boolean | null
          patient_sent?: boolean | null
        }
        Relationships: [
          {
            foreignKeyName: "message_fk_doctor_id_fkey"
            columns: ["fk_doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "message_fk_patient_id_fkey"
            columns: ["fk_patient_id"]
            isOneToOne: false
            referencedRelation: "patient"
            referencedColumns: ["id"]
          }
        ]
      }
      patient: {
        Row: {
          fk_doctor_id: string | null
          id: string
        }
        Insert: {
          fk_doctor_id?: string | null
          id: string
        }
        Update: {
          fk_doctor_id?: string | null
          id?: string
        }
        Relationships: [
          {
            foreignKeyName: "patient_fk_doctor_id_fkey"
            columns: ["fk_doctor_id"]
            isOneToOne: false
            referencedRelation: "doctor"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "patient_id_fkey"
            columns: ["id"]
            isOneToOne: true
            referencedRelation: "person"
            referencedColumns: ["id"]
          }
        ]
      }
      patient_suffers_disease: {
        Row: {
          fk_disease_id: number
          fk_patient_id: string
        }
        Insert: {
          fk_disease_id: number
          fk_patient_id: string
        }
        Update: {
          fk_disease_id?: number
          fk_patient_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "patient_suffers_disease_fk_disease_id_fkey"
            columns: ["fk_disease_id"]
            isOneToOne: false
            referencedRelation: "disease"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "patient_suffers_disease_fk_patient_id_fkey"
            columns: ["fk_patient_id"]
            isOneToOne: false
            referencedRelation: "patient"
            referencedColumns: ["id"]
          }
        ]
      }
      patient_undergoes_surgery: {
        Row: {
          fk_patient_id: string
          fk_surgery_id: number
        }
        Insert: {
          fk_patient_id: string
          fk_surgery_id: number
        }
        Update: {
          fk_patient_id?: string
          fk_surgery_id?: number
        }
        Relationships: [
          {
            foreignKeyName: "patient_undergoes_surgery_fk_patient_id_fkey"
            columns: ["fk_patient_id"]
            isOneToOne: false
            referencedRelation: "patient"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "patient_undergoes_surgery_fk_surgery_id_fkey"
            columns: ["fk_surgery_id"]
            isOneToOne: false
            referencedRelation: "surgery"
            referencedColumns: ["id"]
          }
        ]
      }
      person: {
        Row: {
          address: string | null
          birthdate: string
          dni: string | null
          email: string | null
          encrypted_password: string
          id: string
          name: string
          phone: string | null
          sex: Database["public"]["Enums"]["sex"]
          surname1: string
          surname2: string
        }
        Insert: {
          address?: string | null
          birthdate: string
          dni?: string | null
          email?: string | null
          encrypted_password: string
          id?: string
          name: string
          phone?: string | null
          sex: Database["public"]["Enums"]["sex"]
          surname1: string
          surname2: string
        }
        Update: {
          address?: string | null
          birthdate?: string
          dni?: string | null
          email?: string | null
          encrypted_password?: string
          id?: string
          name?: string
          phone?: string | null
          sex?: Database["public"]["Enums"]["sex"]
          surname1?: string
          surname2?: string
        }
        Relationships: []
      }
      prescription: {
        Row: {
          fk_dose_id: number | null
          fk_patient_id: string | null
          id: number
        }
        Insert: {
          fk_dose_id?: number | null
          fk_patient_id?: string | null
          id?: number
        }
        Update: {
          fk_dose_id?: number | null
          fk_patient_id?: string | null
          id?: number
        }
        Relationships: [
          {
            foreignKeyName: "prescription_fk_dose_id_fkey"
            columns: ["fk_dose_id"]
            isOneToOne: false
            referencedRelation: "dose"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "prescription_fk_patient_id_fkey"
            columns: ["fk_patient_id"]
            isOneToOne: false
            referencedRelation: "patient"
            referencedColumns: ["id"]
          }
        ]
      }
      surgery: {
        Row: {
          datetime: string
          id: number
        }
        Insert: {
          datetime: string
          id?: number
        }
        Update: {
          datetime?: string
          id?: number
        }
        Relationships: []
      }
      user_type: {
        Row: {
          id: number
          name: string | null
        }
        Insert: {
          id?: number
          name?: string | null
        }
        Update: {
          id?: number
          name?: string | null
        }
        Relationships: []
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      generate_user_id: {
        Args: Record<PropertyKey, never>
        Returns: string
      }
      get_current_role: {
        Args: Record<PropertyKey, never>
        Returns: string
      }
      get_current_user: {
        Args: Record<PropertyKey, never>
        Returns: string
      }
      insert_person_records: {
        Args: Record<PropertyKey, never>
        Returns: undefined
      }
      test_no_arguments: {
        Args: Record<PropertyKey, never>
        Returns: string
      }
      test_no_parameters: {
        Args: Record<PropertyKey, never>
        Returns: string
      }
      test_no_parameters_table: {
        Args: Record<PropertyKey, never>
        Returns: {
          foo: number
          bar: string
          bool: boolean
        }[]
      }
      test_parameters_table: {
        Args: {
          a: number
          b: number
        }
        Returns: {
          sum: number
          difference: number
        }[]
      }
    }
    Enums: {
      sex: "MALE" | "FEMALE"
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

export type Tables<
  PublicTableNameOrOptions extends
    | keyof (Database["public"]["Tables"] & Database["public"]["Views"])
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof (Database[PublicTableNameOrOptions["schema"]]["Tables"] &
        Database[PublicTableNameOrOptions["schema"]]["Views"])
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? (Database[PublicTableNameOrOptions["schema"]]["Tables"] &
      Database[PublicTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : PublicTableNameOrOptions extends keyof (Database["public"]["Tables"] &
      Database["public"]["Views"])
  ? (Database["public"]["Tables"] &
      Database["public"]["Views"])[PublicTableNameOrOptions] extends {
      Row: infer R
    }
    ? R
    : never
  : never

export type TablesInsert<
  PublicTableNameOrOptions extends
    | keyof Database["public"]["Tables"]
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicTableNameOrOptions["schema"]]["Tables"]
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? Database[PublicTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : PublicTableNameOrOptions extends keyof Database["public"]["Tables"]
  ? Database["public"]["Tables"][PublicTableNameOrOptions] extends {
      Insert: infer I
    }
    ? I
    : never
  : never

export type TablesUpdate<
  PublicTableNameOrOptions extends
    | keyof Database["public"]["Tables"]
    | { schema: keyof Database },
  TableName extends PublicTableNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicTableNameOrOptions["schema"]]["Tables"]
    : never = never
> = PublicTableNameOrOptions extends { schema: keyof Database }
  ? Database[PublicTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : PublicTableNameOrOptions extends keyof Database["public"]["Tables"]
  ? Database["public"]["Tables"][PublicTableNameOrOptions] extends {
      Update: infer U
    }
    ? U
    : never
  : never

export type Enums<
  PublicEnumNameOrOptions extends
    | keyof Database["public"]["Enums"]
    | { schema: keyof Database },
  EnumName extends PublicEnumNameOrOptions extends { schema: keyof Database }
    ? keyof Database[PublicEnumNameOrOptions["schema"]]["Enums"]
    : never = never
> = PublicEnumNameOrOptions extends { schema: keyof Database }
  ? Database[PublicEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : PublicEnumNameOrOptions extends keyof Database["public"]["Enums"]
  ? Database["public"]["Enums"][PublicEnumNameOrOptions]
  : never
